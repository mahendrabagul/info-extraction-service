package io.github.knowmyminister.infoextractionservice.util;

import io.github.knowmyminister.infoextractionservice.domain.Minister;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelDataReader {

    public static void main(String[] args) throws IOException, InvalidFormatException
    {
        List<Minister> ministers = read("src/main/resources/minister-template.xlsx");
        // System.out.println(JSONGenerator.generate(ministers));
    }

    public static List<Minister> read(String sample_xlsx_file_path) throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(sample_xlsx_file_path));
        List<Minister> ministers = new ArrayList<>();
        for (Sheet sheet : workbook)
        {
            processSheet(sheet, ministers);
        }
        workbook.close();
        return ministers;
    }

    private static String getCellValue(Cell cell)
    {
        if (Objects.isNull(cell)) { return ""; }
        String returnValue = null;
        switch (cell.getCellTypeEnum())
        {
            case BOOLEAN:
                returnValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case STRING:
                returnValue = String.valueOf(cell.getRichStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { returnValue = String.valueOf(cell.getDateCellValue()); }
                else
                {
                    returnValue = String.valueOf((int) cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                returnValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                returnValue = "";
                break;
            default:
                returnValue = "";
        }
        return returnValue;
    }

    private static List<Minister> processSheet(Sheet sheet, List<Minister> ministers) throws IOException
    {
        int rowCount = 0;
        for (Row row : sheet)
        {
            if (rowCount++ == 0) { continue; }
            ministers.add(prepareMinister(row));
        }
        return ministers;
    }

    private static Minister prepareMinister(Row row)
    {
        int columnCount = 0;
        Minister minister = new Minister();
        minister.setId(getCellValue(row.getCell(columnCount++)));
        minister.setSalutation(getCellValue(row.getCell(columnCount++)));
        minister.setFullName(getCellValue(row.getCell(columnCount++)));
        minister.setBrief(getCellValue(row.getCell(columnCount++)));
        minister.setEducation(getCellValue(row.getCell(columnCount++)));
        minister.setCurrentDesignation(getCellValue(row.getCell(columnCount++)));
        minister.setBorn(getCellValue(row.getCell(columnCount++)));
        minister.setParents(getCellValue(row.getCell(columnCount++)));
        minister.setSpouse(getCellValue(row.getCell(columnCount++)));
        minister.setOfficialSite(getCellValue(row.getCell(columnCount++)));
        minister.setParty(getCellValue(row.getCell(columnCount++)));
        minister.setWikipediaUrl(getCellValue(row.getCell(columnCount++)));
        minister.setFacebookUrl(getCellValue(row.getCell(columnCount++)));
        minister.setInstagramUrl(getCellValue(row.getCell(columnCount++)));
        minister.setGooglePlusUrl(getCellValue(row.getCell(columnCount++)));
        minister.setLinkedInUrl(getCellValue(row.getCell(columnCount++)));
        minister.setTwitterUrl(getCellValue(row.getCell(columnCount++)));
        minister.setYoutubeUrl(getCellValue(row.getCell(columnCount++)));
        minister.setSpeechUrl(getCellValue(row.getCell(columnCount++)));
        minister.setProfileUrl(getCellValue(row.getCell(columnCount++)));
        minister.setPartyImageUrl(getCellValue(row.getCell(columnCount++)));
        minister.setConstituency(getCellValue(row.getCell(columnCount++)));
        minister.setAddress(getCellValue(row.getCell(columnCount++)));
        return minister;
    }
}