package com.student.management.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PDFGenerationService {

    public byte[] generateCourseSchedulePdf(String courseName, List<String> schedules) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Create PDF Document
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            // Open the document to write
            document.open();

            // Add Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Course Schedule: " + courseName, titleFont));
            document.add(new Paragraph(" ")); // Empty line for spacing

            // Add each schedule entry as a list item
            Font scheduleFont = new Font(Font.FontFamily.HELVETICA, 12);
            for (String schedule : schedules) {
                document.add(new Paragraph(schedule, scheduleFont));
            }

            // Close the document
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

}
