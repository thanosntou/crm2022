package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.exportfile.Export;
import com.ntouzidis.crm2022.module.exportfile.ExportService;
import com.ntouzidis.crm2022.module.importfile.ExportResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
