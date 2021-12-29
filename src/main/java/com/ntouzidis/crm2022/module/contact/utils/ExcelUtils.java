package com.ntouzidis.crm2022.module.contact.utils;

import com.ntouzidis.crm2022.module.contact.Contact;
import com.ntouzidis.crm2022.module.contact.Country;
import com.ntouzidis.crm2022.module.contact.enums.ContactExcelColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtils {

  public static final String ARIAL = "Arial";
  public static final int HEADER_CELL_FONT_SIZE = 11;
  public static final int CONTACT_CELL_FONT_SIZE = 10;
  public static final String FILE_NAME = "Contacts_Export.xlsx";
  private static CellStyle headerStyle;
  private static CellStyle contactStyle;

  public static File generateFile(List<Contact> contacts) {
    // Create workbook
    var workbook = new XSSFWorkbook();
    // Create sheet
    var sheet = ExcelUtils.createSheet(workbook);
    // Create header row
    ExcelUtils.createHeader(sheet);

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
          createCell(contactRow, cellIndex.getAndIncrement(), contact.email(), contactStyle);
          var country = ofNullable(contact.country()).map(Country::getName).orElse(null);
          createCell(contactRow, cellIndex.getAndIncrement(), country, contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.skype(), contactStyle);
          var viber = ofNullable(contact.viber()).map(Object::toString).orElse(null);
          createCell(contactRow, cellIndex.getAndIncrement(), viber, contactStyle);
          var whatsapp = ofNullable(contact.whatsApp()).map(Object::toString).orElse(null);
          createCell(contactRow, cellIndex.getAndIncrement(), whatsapp, contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.weChat(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.linkedIn(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.businessType().name(), contactStyle);
          createCell(contactRow, cellIndex.getAndIncrement(), contact.comments(), contactStyle);

          cellIndex.set(0);
        });

    return generateXlsxFile(workbook);
  }

  // Read operations
  public static boolean isXlsx(String fileName) {
    return fileName.endsWith(".xlsx");
  }

  public static Workbook toWorkbook(MultipartFile file) {
    try (InputStream excelIs = file.getInputStream()) {
      try (Workbook wb = WorkbookFactory.create(excelIs)) {
        return wb;
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert MultipartFile to workbook", e);
    }
  }

  public static Workbook toWorkbook(byte[] bytes) {
    try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes))) {
      return workbook;
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert bytes to workbook", e);
    }
  }

  public static byte[] toBytes(Workbook workbook) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      workbook.write(bos);
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert bytes to workbook", e);
    } finally {
      try {
        bos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return bos.toByteArray();
  }

  public static Sheet getFirstSheet(Workbook workbook) {
    return workbook.getSheetAt(0);
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
        row.getCell(6).getStringCellValue(),
        String.valueOf(
            Double.valueOf(row.getCell(7).getNumericCellValue())
                .longValue()), // this was needed to copy properly the number, else iit got
        // 6.955555555E
        String.valueOf(Double.valueOf(row.getCell(8).getNumericCellValue()).longValue()),
        row.getCell(9).getStringCellValue(),
        row.getCell(10).getStringCellValue(),
        row.getCell(11).getStringCellValue(),
        row.getCell(12).getStringCellValue());
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

  public static File generateXlsxFile(Workbook workbook) {
    var currDir = new File(".");
    String path = currDir.getAbsolutePath();
    String fileLocation = path.substring(0, path.length() - 1) + FILE_NAME;

    FileOutputStream outputStream;
    try {
      outputStream = new FileOutputStream(fileLocation);
      workbook.write(outputStream);
      workbook.close();
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate xslx file", e);
    }
    return new File(fileLocation);
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
