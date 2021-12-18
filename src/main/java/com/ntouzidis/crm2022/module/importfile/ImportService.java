package com.ntouzidis.crm2022.module.importfile;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ImportService {

  private final ImportRepository importRepository;

  public void createOne(String createdBy, byte[] bytes, Integer importedCount) {
    var createdAt = ZonedDateTime.now(ZoneId.of("Europe/Athens"));
    var name = DateTimeFormatter.ofPattern("yyyyMMdd_hhmm").format(createdAt);
    importRepository.saveNew(name, createdAt, createdBy, bytes, importedCount);
  }

  public List<Import> getAll() {
    return importRepository.getAll();
  }

  public Import getOne(Long id) {
    return importRepository
        .findOne(id)
        .orElseThrow(
            () -> new NotFoundException(String.format("Import with id [%d] not found", id)));
  }

  public void delete(Long id) {
    importRepository.delete(id);
  }
}
