package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.contact.Contact;
import com.ntouzidis.crm2022.module.contact.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EmailRepository {

  private final EmailJpaRepository emailJpaRepository;

  private final ContactRepository contactRepository;

  public List<Email> getAll(boolean withRecipients) {
    return emailJpaRepository.findAll().stream()
        .map(
            entity -> {
              Contact contact = null;
              if (withRecipients) {
                contact = contactRepository.findOne(entity.getRecipient()).orElse(null);
              }
              return Email.fromValues(
                  entity.getId(),
                  entity.getEventId(),
                  contact,
                  entity.getSubject(),
                  entity.getCreatedOn(),
                  entity.getContent());
            })
        .toList();
  }

  public Optional<Email> findOne(Long id) {
    return emailJpaRepository.findById(id).map(EmailEntity::toDomain);
  }

  @Transactional
  public Email saveNew(Integer eventId, Long recipient, String subject, String content) {
    var createdOn = ZonedDateTime.now(ZoneId.of("Europe/Athens"));
    return emailJpaRepository
        .save(EmailEntity.create(eventId, recipient, subject, createdOn, content))
        .toDomain();
  }

  public Integer getLatestEventId() {
    return emailJpaRepository.findTopByOrderByEventIdDesc().map(EmailEntity::getEventId).orElse(0);
  }
}
