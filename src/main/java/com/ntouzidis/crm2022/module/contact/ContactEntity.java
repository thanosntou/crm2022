package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
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

  @Email
  @Column(name = "email")
  private String email;

  @Column(name = "country")
  private String country;

  @Column(name = "skype")
  private String skype;

  @Column(name = "viber")
  private String viber;

  @Column(name = "whats_app")
  private String whatsApp;

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
        this.website,
        this.email,
        StringUtils.isNotBlank(this.country) ? Country.fromName(this.country) : null,
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
    entity.email = contact.email();
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

  public void update(
      String company,
      String name,
      String surname,
      String website,
      String email,
      String country,
      String skype,
      String viber,
      String whatsApp,
      String weChat,
      String linkedIn,
      BusinessType businessType,
      String comments) {
    this.company = company;
    this.name = name;
    this.surname = surname;
    this.website = website;
    this.email = email;
    this.country = country;
    this.skype = skype;
    this.viber = viber;
    this.whatsApp = whatsApp;
    this.weChat = weChat;
    this.linkedIn = linkedIn;
    this.businessType = businessType;
    this.comments = comments;
  }
}
