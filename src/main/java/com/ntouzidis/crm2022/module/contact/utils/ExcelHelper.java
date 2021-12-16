package com.ntouzidis.crm2022.module.contact.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelHelper {

  public static boolean isXlsx(String fileName) {
    return fileName.substring(fileName.length() - 5, fileName.length()).equals(".xlsx");
  }

  public static Sheet getFirstSheet(MultipartFile file) {
    try (InputStream excelIs = file.getInputStream()) {
      try (Workbook wb = WorkbookFactory.create(excelIs)) {
        return wb.getSheetAt(0);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  public static List<Row> getContactRows(Sheet sheet) {
    var rows =
        StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sheet.rowIterator(), Spliterator.ORDERED),
                false)
            .collect(Collectors.toList());

    rows.remove(0);
    return rows;
  }

  public static List<Object> getContactRowValues(Row row) {
    String companyCellValue = row.getCell(0).getStringCellValue();
    String nameCellValue = row.getCell(1).getStringCellValue();
    String surnameCellValue = row.getCell(2).getStringCellValue();
    String countryCellValue = row.getCell(3).getStringCellValue();
    String websiteCellValue = row.getCell(4).getStringCellValue();
    String skypeCellValue = row.getCell(5).getStringCellValue();
    Long viberCellValue =
        Double.valueOf(row.getCell(6).getNumericCellValue()).longValue();
    Long whatsAppCellValue =
        Double.valueOf(row.getCell(7).getNumericCellValue()).longValue();
    String weChatCellValue = row.getCell(8).getStringCellValue();
    String linkedInCellValue = row.getCell(9).getStringCellValue();
    String businessTypeCellValue = row.getCell(10).getStringCellValue();
    String commentCellValue = row.getCell(11).getStringCellValue();

    return List.of(companyCellValue,
        nameCellValue,
        surnameCellValue,
        countryCellValue,
        websiteCellValue,
        skypeCellValue,
        viberCellValue,
        whatsAppCellValue,
        weChatCellValue,
        linkedInCellValue,
        businessTypeCellValue,
        commentCellValue);
  }
}
