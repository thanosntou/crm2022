package com.ntouzidis.crm2022.module.contact.utils;

import com.ntouzidis.crm2022.module.contact.Contact;
import com.ntouzidis.crm2022.module.contact.Country;
import com.ntouzidis.crm2022.module.contact.enums.ContactExcelColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelGenerator {

  public static final String ARIAL = "Arial";
  public static final int HEADER_CELL_FONT_SIZE = 11;
  public static final int CONTACT_CELL_FONT_SIZE = 10;
  private static CellStyle headerStyle;
  private static CellStyle contactStyle;

  public static void generate(List<Contact> contacts) {
    // Create workbook
    var workbook = new XSSFWorkbook();
    // Create sheet
    var sheet = ExcelGenerator.createSheet(workbook);
    // Create header row
    ExcelGenerator.createHeader(sheet);

    // Create contacts rows and cells
    var rowIndex = new AtomicInteger(1);
    var cellIndex = new AtomicInteger(0);
    createContactStyle(workbook);
    contacts.forEach(
        contact -> {
          var contactRow = sheet.createRow(rowIndex.getAndIncrement());
          createCell(contactRow, cellIndex.getAndIncrement(), contact.company(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.name(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.surname(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.website(), contactStyle);
          createCell(
              contactRow,
              cellIndex.getAndIncrement(),
              ofNullable(contact.country()).map(Country::getName).orElse(null),
              contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.skype(), contactStyle);
          createCell(
              contactRow,
              cellIndex.getAndIncrement(),
              ofNullable(contact.viber()).map(Object::toString).orElse(null),
              contactStyle);
          createCell(
              contactRow,
              cellIndex.getAndIncrement(),
              ofNullable(contact.whatsApp()).map(Object::toString).orElse(null),
              contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.weChat(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.linkedIn(), contactStyle);
          createCell(
              contactRow, cellIndex.getAndIncrement(), contact.businessType().name(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.comments(), contactStyle);
          cellIndex.set(0);
        });

    ExcelGenerator.generateXlsxFile(workbook);
  }

  // Read operations
  public static boolean isXlsx(String fileName) {
    return fileName.endsWith(".xlsx");
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

  public static List<String> getContactRowValues(Row row) {
    return List.of(
        row.getCell(0).getStringCellValue(),
        row.getCell(1).getStringCellValue(),
        row.getCell(2).getStringCellValue(),
        row.getCell(3).getStringCellValue(),
        row.getCell(4).getStringCellValue(),
        row.getCell(5).getStringCellValue(),
        String.valueOf(row.getCell(6).getNumericCellValue()),
        String.valueOf(row.getCell(7).getNumericCellValue()),
        row.getCell(8).getStringCellValue(),
        row.getCell(9).getStringCellValue(),
        row.getCell(10).getStringCellValue(),
        row.getCell(11).getStringCellValue());
  }

  // Write operations
  public static Sheet createSheet(Workbook workbook) {
    var sheet = workbook.createSheet("Contacts");
    sheet.setColumnWidth(0, 5120);
    sheet.setColumnWidth(1, 4096);
    sheet.setColumnWidth(2, 4096);
    sheet.setColumnWidth(3, 6400);
    sheet.setColumnWidth(4, 4096);
    sheet.setColumnWidth(5, 4096);
    sheet.setColumnWidth(6, 4096);
    sheet.setColumnWidth(7, 4096);
    sheet.setColumnWidth(8, 4096);
    sheet.setColumnWidth(9, 4096);
    sheet.setColumnWidth(10, 4096);
    sheet.setColumnWidth(11, 10240);
    return sheet;
  }

  public static void createHeader(Sheet sheet) {
    var headerRow = sheet.createRow(0);
    // Create header cells
    var hIndex = new AtomicInteger(0);
    createHeaderStyle(sheet.getWorkbook());
    ContactExcelColumn.getColumns()
        .forEach(column -> createCell(headerRow, hIndex.getAndIncrement(), column, headerStyle));
  }

  private static void createCell(Row row, Integer index, String value, CellStyle style) {
    var cell11 = row.createCell(index);
    cell11.setCellValue(value);
    cell11.setCellStyle(style);
  }

  public static void generateXlsxFile(Workbook workbook) {
    var currDir = new File(".");
    String path = currDir.getAbsolutePath();
    String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

    FileOutputStream outputStream;
    try {
      outputStream = new FileOutputStream(fileLocation);
      workbook.write(outputStream);
      workbook.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void createHeaderStyle(Workbook workbook) {
    headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    var font = workbook.createFont();
    font.setFontName(ARIAL);
    font.setFontHeightInPoints((short) HEADER_CELL_FONT_SIZE);
    font.setBold(true);
    headerStyle.setFont(font);
  }

  public static void createContactStyle(Workbook workbook) {
    contactStyle = workbook.createCellStyle();
    var font = workbook.createFont();
    font.setFontName(ARIAL);
    font.setFontHeightInPoints((short) CONTACT_CELL_FONT_SIZE);
    font.setBold(false);
    contactStyle.setFont(font);
  }
}
