package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;

import javax.validation.constraints.NotNull;

public record ContactForm(@NotNull String company, String name, String surname,
                          String website, @NotNull String country, String skype,
                          Long viber, Long whatsApp, String weChat,
                          String linkedIn, @NotNull BusinessType businessType, String comments) {
}
