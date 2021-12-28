package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.ContactRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

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
        contact -> {
          var message = new SimpleMailMessage();
          message.setTo(contact.email());
          message.setSubject(form.subject());
          message.setText(form.content());
          emailSender.send(message);
          emailRepository.saveNew(newEventId, contact.id(), form.subject(), form.content());
        });
  }

  public void send(String from, String to, String subject, String content, String attachmentName, byte[] attachment) {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, true);
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content);
      helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    emailSender.send(message);
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
