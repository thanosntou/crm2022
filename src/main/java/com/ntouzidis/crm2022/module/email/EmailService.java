package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.ContactRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

  private final EmailRepository emailRepository;

  private final ContactRepository contactRepository;

  private final JavaMailSender emailSender;

  public void send(EmailForm form) {
    var newEventId = emailRepository.getLatestEventId() + 1;

    var contacts =
        form.all()
            ? contactRepository.getAll()
            : form.to().stream()
            .map(recipientId -> contactRepository.findOne(recipientId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id [%s] not found", recipientId))))
            .toList();

    contacts.forEach(
        recipient -> {
          var message = new SimpleMailMessage();
          message.setTo("thanos_nt@yahoo.gr"); // todo the contacts don't have email now
          message.setSubject(form.subject());
          message.setText(form.content());
          emailSender.send(message);
          emailRepository.saveNew(newEventId, recipient.id(), form.subject(), form.content());
        });
  }

  public List<Email> getAll() {
    return emailRepository.getAll(true);
  }

  public Email getOne(Long id) {
    return emailRepository
        .findOne(id)
        .orElseThrow(
            () -> new NotFoundException(String.format("Email with id [%s] not found", id)));
  }
}
