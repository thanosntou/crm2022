package com.ntouzidis.crm2022.module.importfile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Import {

  private Long id;

  private String name;

  private ZonedDateTime createdAt;

  private String createdBy;

  private Workbook file;

  private Integer importedCount;

  public static Import fromValues(
      Long id, String name, ZonedDateTime createdAt, String createdBy, Workbook workbook, Integer importedCount) {
    return new Import(id, name, createdAt, createdBy, workbook, importedCount);
  }
}
