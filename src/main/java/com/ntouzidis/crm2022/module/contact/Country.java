package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public record Country(String code, String name) {

  public static List<Country> getSupportedCountries() {
    return Arrays.stream(Locale.getISOCountries())
        .map(
            l -> {
              Locale obj = new Locale("", l);
              return new Country(obj.getCountry(), obj.getDisplayCountry());
            })
        .toList();
  }

  public static Country fromCode(String code) {
    return getSupportedCountries()
        .stream()
        .filter(c -> c.code().equals(code))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(String.format("Country with code [%s] is not supported", code)));
  }

  public static Country fromName(String name) {
    return getSupportedCountries()
        .stream()
        .filter(c -> c.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(String.format("Country with name [%s] is not supported", name)));
  }
}
