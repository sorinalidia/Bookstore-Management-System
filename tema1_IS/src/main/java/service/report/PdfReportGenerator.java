package service.report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import model.Order;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfReportGenerator {

    public static void generatePdfReport( List<Order> orders) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filePath = "D:\\IS\\IS_project\\tema1_IS\\report" + timestamp + ".pdf";


        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Order Report"));
            document.add(new Paragraph("-------------------------------"));

            for (Order order : orders) {
                document.add(new Paragraph("Order id: " + order.getId()));
                document.add(new Paragraph("Customer id: " + order.getCustomerId()));
                document.add(new Paragraph("Book id: " + order.getBookId()));
                document.add(new Paragraph("Purchase date: " + order.getPurchaseDate()));
                document.add(new Paragraph("-------------------------------"));
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
