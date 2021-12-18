package com.ntouzidis.crm2022.module.exportfile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Export {

  private Long id;

  private String name;

  private ZonedDateTime createdAt;

  private String createdBy;

  private Workbook file;

  private Integer exportedCount;

  public static Export fromValues(
      Long id,
      String name,
      ZonedDateTime createdAt,
      String createdBy,
      Workbook workbook,
      Integer exportedCount) {
    return new Export(id, name, createdAt, createdBy, workbook, exportedCount);
  }
}
