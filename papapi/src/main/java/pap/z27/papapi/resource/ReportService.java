package pap.z27.papapi.resource;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.Chunk;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import pap.z27.papapi.domain.subclasses.NameGrade;

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
        document.add(new Paragraph(Chunk.NEWLINE));

        Paragraph uczestnicyHeader = new Paragraph(new Chunk("II: Uczestnicy\n", headerFont));
        document.add(uczestnicyHeader);

        float[] columnDefinitionSize = {33.33F, 33.33F, 33.33F};
        PdfPTable table = new PdfPTable(columnDefinitionSize);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setTotalWidth(width - 20);
        table.setLockedWidth(true);

        PdfPCell cell = new PdfPCell();
        cell.setColspan(columnDefinitionSize.length);
        table.addCell(cell);
        table.addCell(new Phrase("Louis Pasteur", textFont));
        table.addCell(new Phrase("Albert Einstein", textFont));
        table.addCell(new Phrase("Isaac Newton", textFont));
        table.addCell(new Phrase("8, Rabic street", textFont));
        table.addCell(new Phrase("2 Photons Avenue", textFont));
        table.addCell(new Phrase("32 Gravitation Court", textFont));
        table.addCell(new Phrase("39100 Dole France", textFont));
        table.addCell(new Phrase("12345 Ulm Germany", textFont));
        table.addCell(new Phrase("45789 Cambridge  England", textFont));

        document.add(table);


//        Paragraph signature = new Paragraph("........", textFont);
//        signature.setAlignment(Element.ALIGN_RIGHT);

//        document.add(title);
//        document.add(Chunk.NEWLINE);
//        document.add(text);
//        document.add(Chunk.NEWLINE);
//        document.add(signature);

        document.close();
    }
}
