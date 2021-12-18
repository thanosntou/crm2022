package com.ntouzidis.crm2022.module.exportfile;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ExportRepository {

  private final ExportJpaRepository exportJpaRepository;

  public List<Export> getAll() {
    return exportJpaRepository.findAll().stream().map(ExportEntity::toDomain).toList();
  }

  public Optional<Export> findOne(Long id) {
    return exportJpaRepository.findById(id).map(ExportEntity::toDomain);
  }

  public Export saveNew(
      @NonNull String name,
      @NonNull ZonedDateTime createdAt,
      @NonNull String createdBy,
      byte[] file,
      Integer exportedCount) {
    return exportJpaRepository
        .save(ExportEntity.create(name, createdAt, createdBy, file, exportedCount))
        .toDomain();
  }
}
