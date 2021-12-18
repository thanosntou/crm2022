package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.contact.Contact;
import com.ntouzidis.crm2022.module.contact.ContactForm;
import com.ntouzidis.crm2022.module.contact.ContactService;
import com.ntouzidis.crm2022.module.contact.Country;
import com.ntouzidis.crm2022.module.contact.enums.ExportType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.ntouzidis.crm2022.module.common.constants.AuthorizationConstants.ADMIN_OR_ROOT;
import static com.ntouzidis.crm2022.module.common.constants.ControllerPaths.CONTACT_CONTROLLER_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = CONTACT_CONTROLLER_PATH, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ContactController {

  private final ContactService contactService;

  @PostMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public Contact createContact(@RequestBody @Valid ContactForm form) {
    return contactService.createOne(form);
  }

  @PostMapping(value = "/import")
  @PreAuthorize(ADMIN_OR_ROOT)
  public List<Contact> createContactsFromFile(
      @RequestParam(name = "file") MultipartFile file,
      @RequestParam(name = "id", required = false) Long id) {
    return contactService.importFromFile(file);
  }

  @GetMapping("/export")
  @PreAuthorize(ADMIN_OR_ROOT)
  public ResponseEntity<Resource> exportFile(@RequestParam ExportType type)
      throws FileNotFoundException {

    if (type == ExportType.EMAIL) {
      contactService.exportAndSendToEmail();
      return ResponseEntity.ok().build();

    } else if (type == ExportType.DOWNLOAD) {
      var file = contactService.exportAndDownload();
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getName() + "\"")
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .contentLength(file.length())
          .body(new InputStreamResource(new FileInputStream(file)));

    } else {
      throw new UnsupportedOperationException(
          String.format("Export type [%s] is not supported", type));
    }
  }

  @GetMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public List<Contact> getAllContacts() {
    return contactService.getAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize(ADMIN_OR_ROOT)
  public Contact getOneContact(@PathVariable Long id) {
    return contactService.getOne(id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize(ADMIN_OR_ROOT)
  public void deleteContact(@PathVariable Long id) {
    contactService.delete(id);
  }

  @GetMapping("/countries")
  public List<Country> getAllSupportedCountries() {
    return contactService.getSupportedCountries();
  }

  @GetMapping("/business-types")
  public List<BusinessType> getAllSupportedBusinessTypes() {
    return contactService.getSupportedBusinessTypes();
  }
}
