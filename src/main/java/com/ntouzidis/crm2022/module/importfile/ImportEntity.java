package com.ntouzidis.crm2022.module.importfile;

import com.ntouzidis.crm2022.module.contact.utils.ExcelUtils;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "import")
public class ImportEntity implements Serializable {

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

  @Column(name = "imported_count")
  private Integer importedCount;

  public Import toDomain() {
    return Import.fromValues(
        id, name, createdAt, createdBy, ExcelUtils.toWorkbook(file), importedCount);
  }

  public static ImportEntity create(
      @NonNull String name,
      @NonNull ZonedDateTime createdAt,
      @NonNull String createdBy,
      byte[] file,
      @NonNull Integer importedCount) {
    var entity = new ImportEntity();
    entity.name = name;
    entity.createdAt = createdAt;
    entity.createdBy = createdBy;
    entity.file = file;
    entity.importedCount = importedCount;
    return entity;
  }
}
