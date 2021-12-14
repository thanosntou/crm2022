package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "contact")
public class ContactEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "company")
  private String company;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "website")
  private String website;

  @Column(name = "country")
  private String country;

  @Column(name = "skype")
  private String skype;

  @Column(name = "viber")
  private Long viber;

  @Column(name = "whats_app")
  private Long whatsApp;

  @Column(name = "we_chat")
  private String weChat;

  @Column(name = "linked_in")
  private String linkedIn;

  @Column(name = "business_type")
  @Enumerated(EnumType.STRING)
  private BusinessType businessType;

  @Column(name = "comments")
  private String comments;

  public Contact toDomain() {
    return new Contact(
        this.id,
        this.company,
        this.name,
        this.surname,
        StringUtils.isNotBlank(this.country) ? Country.fromName(this.country) : null,
        this.website,
        this.skype,
        this.viber,
        this.whatsApp,
        this.weChat,
        this.linkedIn,
        this.businessType,
        this.comments);
  }

  public static ContactEntity fromDomain(Contact contact) {
    var entity = new ContactEntity();
    entity.company = contact.company();
    entity.name = contact.name();
    entity.surname = contact.surname();
    entity.website = contact.website();
    entity.country = Optional.ofNullable(contact.country()).map(Country::getName).orElse(null);
    entity.skype = contact.skype();
    entity.viber = contact.viber();
    entity.whatsApp = contact.whatsApp();
    entity.weChat = contact.weChat();
    entity.linkedIn = contact.linkedIn();
    entity.businessType = contact.businessType();
    entity.comments = contact.comments();
    return entity;
  }
}
