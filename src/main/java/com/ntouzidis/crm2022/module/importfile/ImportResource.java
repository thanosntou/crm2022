package com.ntouzidis.crm2022.module.importfile;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImportResource(Long id, String name, ZonedDateTime createdAt, String createdBy, Integer importedCount) {
}
