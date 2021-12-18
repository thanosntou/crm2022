package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.contact.Contact;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

  private Long id;

  private Integer eventId;

  private Contact recipient;

  private String subject;

  private ZonedDateTime createdOn;

  private String content;

  public static Email fromValues(Long id, Integer eventId, Contact recipient, String subject, ZonedDateTime createdOn, String content) {
    return new Email(id, eventId, recipient, subject, createdOn, content);
  }

  public void setRecipient(Contact recipient) {
    this.recipient = recipient;
  }
}
