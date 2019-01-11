/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.artec.apps.facebook.reports;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelReport {

    public static void create(String FILE_NAME, String sheetName, List<Object[]> dataX) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        for (Object[] datatype : dataX) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String)
                    cell.setCellValue((String) field);
                else if (field instanceof Integer)
                    cell.setCellValue((Integer) field);
            }
        }
        FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
        workbook.write(outputStream);
        workbook.close();
    }
}