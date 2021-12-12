package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;

public record ContactForm(String company, String name, String surname,
                          String website, String country, String skype,
                          String viber, String whatsApp, String weChat,
                          String linkedIn, BusinessType businessType, String comments) {
}
