package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;

public record Contact(Long id, String company, String name, String surname,
                      Country country, String website, String skype,
                      String viber, String whatsApp, String weChat,
                      String linkedIn, BusinessType businessType, String comments) {

  public static Contact create(ContactForm form) {
    return  new Contact(
        null,
        form.company(),
        form.name(),
        form.surname(),
        Country.fromCode(form.country()),
        form.website(),
        form.skype(),
        form.viber(),
        form.whatsApp(),
        form.weChat(),
        form.linkedIn(),
        form.businessType(),
        form.comments());
  }
}
