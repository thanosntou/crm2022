package com.ntouzidis.crm2022.module.exportfile;

import java.time.ZonedDateTime;

public record ExportResource(Long id, String name, ZonedDateTime createdAt, String createdBy, Integer exportedCount) {
}
