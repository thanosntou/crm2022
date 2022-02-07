package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.utils.Iso2Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
@AllArgsConstructor
public class Country {

  private final String code;

  private final String name;

  private final String phonePrefix;

  public static List<Country> getSupportedCountries() {
    return Arrays.stream(Locale.getISOCountries())
        .map(
            l -> {
              Locale obj = new Locale("", l);
              return new Country(
                  obj.getCountry(), obj.getDisplayCountry(), Iso2Phone.getPhone(obj.getCountry()));
            })
        .toList();
  }

  public static Country fromCode(String code) {
    return getSupportedCountries().stream()
        .filter(c -> c.getCode().equals(code))
        .findFirst()
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format("Country with code [%s] is not supported", code)));
  }

  public static Country fromName(String name) {
    return getSupportedCountries().stream()
        .filter(c -> c.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format("Country with name [%s] is not supported", name)));
  }

  public static void validateName(String name) {
    var countryOpt = getSupportedCountries().stream()
        .filter(c -> c.getName().equalsIgnoreCase(name))
        .findFirst();
    if (countryOpt.isEmpty()) {
      throw new NotFoundException(
          String.format("Country with name [%s] is not supported", name));
    }
  }

  public static void validateCode(String code) {
    var countryOpt = getSupportedCountries().stream()
        .filter(c -> c.getCode().equalsIgnoreCase(code))
        .findFirst();
    if (countryOpt.isEmpty()) {
      throw new NotFoundException(
          String.format("Country with code [%s] is not supported", code));
    }
  }
}
