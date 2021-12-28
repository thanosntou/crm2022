package com.ntouzidis.crm2022.module.exportfile;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.common.pojo.Context;
import com.ntouzidis.crm2022.module.contact.ContactRepository;
import com.ntouzidis.crm2022.module.contact.utils.ExcelUtils;
import com.ntouzidis.crm2022.module.email.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExportService {

  private final Context context;

  private final EmailService emailService;

  private final ExportRepository exportRepository;

  private final ContactRepository contactRepository;

  @Transactional
  public void exportAndSendToEmail() {
    var contacts = contactRepository.getAll();
    var file = ExcelUtils.generateFile(contacts);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());
      emailService.send(
          "thanosntouzidis@gmail.com",
          "thanos_nt@yahoo.gr",
          "Your Contacts",
          "An excel file with all the contacts is attached.",
          ExcelUtils.FILE_NAME,
          fileBytes);
      createOne(context.getUser().getUsername(), fileBytes, contacts.size());
    } catch (IOException e) {
      throw new RuntimeException("Failed to get the bytes from exported file");
    }
  }

  @Transactional
  public File exportAndDownload() {
    var contacts = contactRepository.getAll();
    var file = ExcelUtils.generateFile(contacts);
    try {
      createOne(
          context.getUser().getUsername(), Files.readAllBytes(file.toPath()), contacts.size());
    } catch (IOException e) {
      throw new RuntimeException("Failed to get the bytes from exported file");
    }
    return file;
  }

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
