package io.github.knowmyminister.infoextractionservice.util;

import io.github.knowmyminister.infoextractionservice.domain.Minister;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {
    private static String[] columns = {"Full Name", "Brief Info", "Designation", "BirthDate", "Political Party", "Spouse", "Website", "Wikipedia", "Facebook", "Instagram", "Google Plus", "Linked In", "Twitter", "Youtube"};

    public static void generate(List<Minister> ministers) throws IOException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Minister");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight((short) 16);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Creating cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with ministers data

        int rowNum = 1;
        for (Minister minister : ministers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(minister.getFullName());
            row.createCell(1).setCellValue(minister.getBriefInfo());
            row.createCell(2).setCellValue(minister.getDesignation());
            row.createCell(3).setCellValue(minister.getBirthDate());
            row.createCell(4).setCellValue(minister.getPoliticalParty());
            row.createCell(5).setCellValue(minister.getSpouse());
            row.createCell(6).setCellValue(minister.getWebsite());
            row.createCell(7).setCellValue(minister.getWikipedia());
            row.createCell(8).setCellValue(minister.getFacebook());
            row.createCell(9).setCellValue(minister.getInstagram());
            row.createCell(10).setCellValue(minister.getGooglePlus());
            row.createCell(11).setCellValue(minister.getLinkedIn());
            row.createCell(12).setCellValue(minister.getTwitter());
            row.createCell(13).setCellValue(minister.getYoutube());
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }
}
