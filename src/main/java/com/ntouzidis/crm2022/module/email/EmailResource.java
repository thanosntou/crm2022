package com.ntouzidis.crm2022.module.email;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmailResource(List<String> to, String subject, String content) {
}
