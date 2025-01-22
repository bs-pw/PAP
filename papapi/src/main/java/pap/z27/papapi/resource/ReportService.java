package pap.z27.papapi.resource;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.LayoutProcessor;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ReportService {
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);

        LayoutProcessor.enableKernLiga();
        String fontDir = "src/main/java/pap/z27/papapi/Noto_Sans/static/";
        FontFactory.register(fontDir + "NotoSans-Regular.ttf", "notoSans");
        FontFactory.register(fontDir + "NotoSans-Bold.ttf", "notoSansBold");


        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font titleFont = FontFactory.getFont("notoSansBold", BaseFont.IDENTITY_H, true, 20);

        Font textFont = FontFactory.getFont("notoSans", BaseFont.IDENTITY_H, true, 12);

        Paragraph title = new Paragraph("PROTOKÓŁ KOŃCOWY", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        Paragraph text = new Paragraph("This is a test.", textFont);
        text.setAlignment(Element.ALIGN_CENTER);

//        Paragraph signature = new Paragraph("........", textFont);
//        signature.setAlignment(Element.ALIGN_RIGHT);

        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(text);
//        document.add(Chunk.NEWLINE);
//        document.add(signature);

        document.close();
    }
}
