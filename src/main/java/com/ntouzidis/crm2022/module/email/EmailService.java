package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.ContactRepository;
import com.ntouzidis.crm2022.module.user.entity.User;
import com.sun.mail.smtp.SMTPSenderFailedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Properties;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

  private final EmailRepository emailRepository;

  private final ContactRepository contactRepository;

  public void send(@NonNull User sender, @NonNull EmailForm form) {
    var newEventId = emailRepository.getLatestEventId() + 1;

    var contacts =
        form.all()
            ? contactRepository.getAllByBusinessType(form.businessType())
            : form.to().stream()
                .map(
                    recipientId ->
                        contactRepository
                            .findOne(recipientId)
                            .orElseThrow(
                                () ->
                                    new NotFoundException(
                                        String.format("User with id [%s] not found", recipientId))))
                .toList();

    contacts.forEach(
        contact -> {
          var javaMailSender = newJavaMailSender(sender.getEmail(), sender.getEmailPass());
          var message = new SimpleMailMessage();
          message.setTo(contact.email());
          message.setSubject(form.subject());
          message.setText(form.content());
          try {
            javaMailSender.send(message);
            emailRepository.saveNew(newEventId, contact.id(), form.subject(), form.content());
          } catch (MailSendException e) {
            log.error("Failed to send email to [{}]", contact.email(), e);
          }
        });
  }

  public void send(
      String from,
      String hostPassword,
      String to,
      String subject,
      String content,
      String attachmentName,
      byte[] attachment) {
    var javaMailSender = newJavaMailSender(from, hostPassword);
    var message = javaMailSender.createMimeMessage();
    try {
      var helper = new MimeMessageHelper(message, true);
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content);
      helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    javaMailSender.send(message);
  }

  private JavaMailSender newJavaMailSender(@NonNull String from, @NonNull String hostPassword) {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    var mailSender = new JavaMailSenderImpl();
    if (from.matches("(.*)gmail(.*)|(.*)GMAIL(.*)")) {
      mailSender.setHost("smtp.gmail.com");
      //    } else if (from.matches("(.*)yahoo(.*)|(.*)YAHOO(.*)")) {
      //      mailSender.setHost("smtp.mail.yahoo.com");
    } else {
      throw new UnsupportedOperationException(String.format("Not supported mail host [%s]", from));
    }
    mailSender.setPort(587);
    mailSender.setUsername(from);
    mailSender.setPassword(hostPassword);
    mailSender.setJavaMailProperties(props);
    return mailSender;
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
