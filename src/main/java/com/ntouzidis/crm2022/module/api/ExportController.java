package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.contact.ContactService;
import com.ntouzidis.crm2022.module.contact.enums.ExportType;
import com.ntouzidis.crm2022.module.exportfile.Export;
import com.ntouzidis.crm2022.module.exportfile.ExportService;
import com.ntouzidis.crm2022.module.exportfile.ExportResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.ntouzidis.crm2022.module.common.constants.AuthorizationConstants.ADMIN_OR_ROOT;
import static com.ntouzidis.crm2022.module.common.constants.ControllerPaths.EXPORT_CONTROLLER_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = EXPORT_CONTROLLER_PATH, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ExportController {

  private final ExportService exportService;

  @PostMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public ResponseEntity<Resource> exportFile(@RequestParam ExportType type)
      throws FileNotFoundException {

    if (type == ExportType.EMAIL) {
      exportService.exportAndSendToEmail();
      return ResponseEntity.ok().build();

    } else if (type == ExportType.DOWNLOAD) {
      var file = exportService.exportAndDownload();
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
  public List<ExportResource> getAll() {
    return exportService.getAll().stream().map(this::toResource).toList();
  }

  @GetMapping("/{id}")
  @PreAuthorize(ADMIN_OR_ROOT)
  public ExportResource getOne(@PathVariable Long id) {
    return toResource(exportService.getOne(id));
  }

  private ExportResource toResource(Export export) {
    return new ExportResource(
        export.getId(),
        export.getName(),
        export.getCreatedAt(),
        export.getCreatedBy(),
        export.getExportedCount());
  }
}
