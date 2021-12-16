package com.ntouzidis.crm2022.module.contact.enums;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum ContactExcelColumn {
  COMPANY("Company"),
  NAME("Name"),
  SURNAME("Surname"),
  WEBSITE("Website"),
  COUNTRY("Country"),
  SKYPE("Skype"),
  VIBER("Viber"),
  WHATS_APP("WhatsApp"),
  WE_CHAT("WeChat"),
  LINKED_IN("LinkedIn"),
  BUSINESS_TYPE("BusinessType"),
  COMMENTS("Comments");

  private final String column;

  ContactExcelColumn(String column) {
    this.column = column;
  }

  public static Set<String> getColumns() {
    return Arrays.stream(ContactExcelColumn.values())
        .map(v -> v.column)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }
}
