package com.ntouzidis.crm2022.module.email;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "email")
public class EmailEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "event_id")
  private Integer eventId;

  @Column(name = "recipient")
  private Long recipient;

  @Column(name = "subject")
  private String subject;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "content")
  private String content;

  public Email toDomain() {
    return Email.fromValues(id, eventId, null, subject, createdOn, content);
  }

  public static EmailEntity create(Integer eventId, Long recipient, String subject, ZonedDateTime createdOn, String content) {
    var entity = new EmailEntity();
    entity.eventId = eventId;
    entity.recipient = recipient;
    entity.subject = subject;
    entity.createdOn = createdOn;
    entity.content = content;
    return entity;
  }
}
