package pap.z27.papapi.resource;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Chunk;
import com.lowagie.text.pdf.LayoutProcessor;
import com.lowagie.text.pdf.PdfWriter;
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

        document.open();
        Font titleFont = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 20);
        Font headerFont = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 16);
        Font textFont = FontFactory.getFont("notoSans", BaseFont.IDENTITY_H, true, 12);

        Paragraph title = new Paragraph("PROTOKÓŁ KOŃCOWY", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Paragraph daneHeader = new Paragraph(new Chunk("I: Dane przedmiotu", headerFont));
        document.add(daneHeader);

        Paragraph daneText = new Paragraph(new Chunk("Przedmiot [KOD]: ", textFont));
        daneText.add(courseTitle + " [" + courseCode + "]\n");
        daneText.add("Semestr: " + semester + "\n");
        daneText.add("Koordynator(rzy): " + String.join(", ", coordinators) + "\n");
        daneText.add("Prowadzący: " + String.join(", ", lecturers) + "\n");
        document.add(daneText);

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
