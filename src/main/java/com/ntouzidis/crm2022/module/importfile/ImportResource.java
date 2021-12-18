package com.ntouzidis.crm2022.module.importfile;

import java.time.ZonedDateTime;

public record ImportResource(Long id, String name, ZonedDateTime createdAt, String createdBy, Integer importedCount) {
}
