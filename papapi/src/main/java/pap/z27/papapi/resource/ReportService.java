package pap.z27.papapi.resource;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.Chunk;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import pap.z27.papapi.domain.subclasses.NameGrade;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;


@Service
public class ReportService {
    public void export(HttpServletResponse response,
                       String semester,
                       String courseCode,
                       List<String> coordinators,
                       List<String> lecturers,
                       List<NameGrade> nameGrades,
                       String courseTitle) throws IOException {
        Document document = new Document(PageSize.A4);

        LayoutProcessor.enableKernLiga();
        String fontDir = "src/main/java/pap/z27/papapi/Noto_Sans/static/";
        FontFactory.register(fontDir + "NotoSans-Regular.ttf", "notoSans");
        FontFactory.register(fontDir + "NotoSans-Bold.ttf", "notoSansBold");


        PdfWriter.getInstance(document, response.getOutputStream());
        float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();

        document.open();
        Font titleFont = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 20);
        Font headerFont = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 16);
        Font textFont = FontFactory.getFont("notoSans", BaseFont.IDENTITY_H, true, 12);
        Font textBold = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 12);

        Paragraph newline = new Paragraph(new Chunk("\n", textFont));

        Paragraph title = new Paragraph("PROTOKÓŁ KOŃCOWY", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(Chunk.NEWLINE));

        Paragraph daneHeader = new Paragraph(new Chunk("I: Dane przedmiotu", headerFont));
        document.add(daneHeader);

        Paragraph daneText = new Paragraph(new Chunk("Przedmiot [KOD]: ", textFont));
        daneText.add(courseTitle + " [" + courseCode + "]\n");
        daneText.add("Semestr: " + semester + "\n");
        daneText.add("Koordynator(rzy): " + String.join(", ", coordinators) + "\n");
        daneText.add("Prowadzący: " + String.join(", ", lecturers) + "\n");
        document.add(daneText);
        document.add(newline);

        Paragraph uczestnicyHeader = new Paragraph(new Chunk("II: Uczestnicy\n", headerFont));
        document.add(uczestnicyHeader);

        float[] columnDefinitionSize = {5F, 15F, 5F};
        PdfPTable table = new PdfPTable(columnDefinitionSize);
        table.setSpacingBefore(10F);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setTotalWidth(width - 70);
        table.setLockedWidth(true);

        table.addCell(new Phrase("Lp.", textBold));
        table.addCell(new Phrase("Imię i Nazwisko", textBold));
        table.addCell(new Phrase("Ocena", textBold));

        for (int i = 0; i < nameGrades.size(); i++) {
            table.addCell(new Phrase((i + 1) + ".", textFont));
            table.addCell(new Phrase(nameGrades.get(i).getName(), textFont));
            table.addCell(new Phrase(String.valueOf(nameGrades.get(i).getGrade()), textFont));
        }

        document.add(table);
        document.add(newline);

        document.add(new Paragraph(new Chunk("III: Podsumowanie\n", headerFont)));
        Paragraph podsumowanie = new Paragraph(new Chunk("Liczba studentów zapisanych: ", textFont));
        podsumowanie.add(nameGrades.size() + "\n");
        podsumowanie.add("Liczba stuentów, którzy ukończyli przedmiot: ");
        podsumowanie.add(String.valueOf(
                nameGrades.stream()
                    .filter(item -> item.getGrade() > 2)
                    .count()
        ) + "\n");
        podsumowanie.add("\nRozkład ocen:\n");

        document.add(podsumowanie);

        PdfPTable gradeTable = new PdfPTable(2);
        gradeTable.setSpacingBefore(10F);
        gradeTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        gradeTable.addCell(new Phrase("Ocena", textBold));
        gradeTable.addCell(new Phrase("Liczba ocen", textBold));

        float epsilon = 0.0000001F;
        List<Float> gradeList = new ArrayList<Float>();
        gradeList.add(2F);
        gradeList.add(3F);
        gradeList.add(3.5F);
        gradeList.add(4F);
        gradeList.add(4.5F);
        gradeList.add(5F);

        for (float grade : gradeList) {
            Integer count = (int)nameGrades.stream()
                    .filter(item -> Math.abs(item.getGrade() - grade) < epsilon)
                    .count();
            gradeTable.addCell(new Phrase(String.valueOf(grade), textFont));
            gradeTable.addCell(new Phrase(String.valueOf(count), textFont));
        }

        document.add(gradeTable);
        document.add(newline);
        document.add(newline);

        Paragraph signaturePlaceholder = new Paragraph(new Chunk(".....................................", textFont));
        signaturePlaceholder.setAlignment(Element.ALIGN_RIGHT);

        Paragraph coordinatorName;

        for (String coordinator : coordinators) {
            coordinatorName = new Paragraph(new Chunk(coordinator, textFont));
            coordinatorName.setAlignment(Element.ALIGN_RIGHT);
            document.add(signaturePlaceholder);
            document.add(coordinatorName);
            document.add(newline);
        }
        document.close();
    }
}
