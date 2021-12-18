package com.ntouzidis.crm2022.module.exportfile;

import com.ntouzidis.crm2022.module.contact.utils.ExcelUtils;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "export")
public class ExportEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Lob
  @Column(name = "file", columnDefinition = "BLOB")
  private byte[] file;

  @Column(name = "exported_count")
  private Integer exportedCount;

  public Export toDomain() {
    return Export.fromValues(
        id, name, createdAt, createdBy, ExcelUtils.toWorkbook(file), exportedCount);
  }

  public static ExportEntity create(
      @NonNull String name,
      @NonNull ZonedDateTime createdAt,
      @NonNull String createdBy,
      byte[] file,
      @NonNull Integer exportedCount) {
    var entity = new ExportEntity();
    entity.name = name;
    entity.createdAt = createdAt;
    entity.createdBy = createdBy;
    entity.file = file;
    entity.exportedCount = exportedCount;
    return entity;
  }
}
