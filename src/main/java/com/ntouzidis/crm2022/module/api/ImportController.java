package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.contact.Contact;
import com.ntouzidis.crm2022.module.contact.ContactService;
import com.ntouzidis.crm2022.module.importfile.Import;
import com.ntouzidis.crm2022.module.importfile.ImportResource;
import com.ntouzidis.crm2022.module.importfile.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ntouzidis.crm2022.module.common.constants.AuthorizationConstants.ADMIN_OR_ROOT;
import static com.ntouzidis.crm2022.module.common.constants.ControllerPaths.IMPORT_CONTROLLER_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = IMPORT_CONTROLLER_PATH, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ImportController {

  private final ImportService importService;

  private final ContactService contactService;

  @PostMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public List<Contact> importContacts(
      @RequestParam(name = "file") MultipartFile file,
      @RequestParam(name = "id", required = false) Long id) {
    return contactService.importFromFile(file);
  }

  @GetMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public List<ImportResource> getAll() {
    return importService.getAll().stream().map(this::toResource).toList();
  }

  @GetMapping("/{id}")
  @PreAuthorize(ADMIN_OR_ROOT)
  public ImportResource getOne(@PathVariable Long id) {
    return toResource(importService.getOne(id));
  }

  private ImportResource toResource(Import anImport) {
    return new ImportResource(
        anImport.getId(),
        anImport.getName(),
        anImport.getCreatedAt(),
        anImport.getCreatedBy(),
        anImport.getImportedCount());
  }
}
