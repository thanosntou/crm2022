package com.ntouzidis.crm2022.module.exportfile;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExportService {

  private final ExportRepository exportRepository;

  public void createOne(String createdBy, byte[] bytes, Integer exportedCount) {
    var createdAt = ZonedDateTime.now(ZoneId.of("Europe/Athens"));
    var name = DateTimeFormatter.ofPattern("yyyyMMdd_hhmm").format(createdAt);
    exportRepository.saveNew(name, createdAt, createdBy, bytes, exportedCount);
  }

  @Transactional(readOnly = true)
  public List<Export> getAll() {
    return exportRepository.getAll();
  }

  @Transactional(readOnly = true)
  public Export getOne(Long id) {
    return exportRepository
        .findOne(id)
        .orElseThrow(
            () -> new NotFoundException(String.format("Export with id [%d] not found", id)));
  }
}
