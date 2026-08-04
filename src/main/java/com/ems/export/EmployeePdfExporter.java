package com.ems.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.ems.entity.Employee;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class EmployeePdfExporter {

    public static ByteArrayInputStream export(List<Employee> employees) {

        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Paragraph title =
                    new Paragraph("Employee Report", headFont);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);

            table.setWidthPercentage(100);

            table.addCell("ID");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Email");
            table.addCell("Department");
            table.addCell("Salary");

            for(Employee emp : employees){

                table.addCell(String.valueOf(emp.getId()));
                table.addCell(emp.getFirstName());
                table.addCell(emp.getLastName());
                table.addCell(emp.getEmail());
                table.addCell(emp.getDepartment());
                table.addCell(String.valueOf(emp.getSalary()));
            }

            document.add(table);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());

    }

}