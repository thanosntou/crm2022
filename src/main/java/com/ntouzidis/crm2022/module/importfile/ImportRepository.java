package com.ntouzidis.crm2022.module.importfile;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ImportRepository {

  private final ImportJpaRepository importJpaRepository;

  @Transactional(readOnly = true)
  public List<Import> getAll() {
    return importJpaRepository.findAll().stream().map(ImportEntity::toDomain).toList();
  }

  @Transactional(readOnly = true)
  public Optional<Import> findOne(Long id) {
    return importJpaRepository.findById(id).map(ImportEntity::toDomain);
  }

  @Transactional
  public void saveNew(
      @NonNull String name,
      @NonNull ZonedDateTime createdAt,
      @NonNull String createdBy,
      byte[] file,
      Integer importedCount) {
    var entity = ImportEntity.create(name, createdAt, createdBy, file, importedCount);
    importJpaRepository.save(entity).toDomain();
  }

  @Transactional
  public void delete(Long id) {
    var entity =
        importJpaRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("Import with id [%d] not found", id)));

    importJpaRepository.delete(entity);
  }
}
