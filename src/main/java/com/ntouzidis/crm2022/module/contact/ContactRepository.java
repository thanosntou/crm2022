package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ContactRepository {

  private final ContactJpaRepository contactJpaRepository;

  public List<Contact> getAll() {
    return contactJpaRepository
        .findAll()
        .stream()
        .sorted(Comparator.comparing(ContactEntity::getCompany))
        .map(ContactEntity::toDomain)
        .toList();
  }

  public List<Contact> search(String value) {
    return contactJpaRepository.searchByValue(StringUtils.lowerCase(value)).stream().map(ContactEntity::toDomain).toList();
  }

  public List<Contact> getAllByBusinessType(BusinessType businessType) {
    return contactJpaRepository.findAllByBusinessType(businessType).stream()
        .sorted(Comparator.comparing(ContactEntity::getCompany))
        .map(ContactEntity::toDomain)
        .toList();
  }

  public Optional<Contact> findOne(Long id) {
    return contactJpaRepository.findById(id).map(ContactEntity::toDomain);
  }

  public Contact saveNew(@NonNull Contact contact) {
    return contactJpaRepository.save(ContactEntity.fromDomain(contact)).toDomain();
  }

  public Contact updateOne(Long id, String company, String name, String surname, String website, String email,
                           String country, String skype, String viber, String whatsApp, String weChat, String linkedIn,
                           BusinessType businessType, String comments) {
    var entity =
        contactJpaRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("Contact with id [%d] not found", id)));
    entity.update(
        company,
        name,
        surname,
        website,
        email,
        country,
        skype,
        viber,
        whatsApp,
        weChat,
        linkedIn,
        businessType,
        comments);

    return contactJpaRepository.save(entity).toDomain();
  }

  public void delete(Long id) {
    var entity =
        contactJpaRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("Contact with id [%d] not found", id)));

    contactJpaRepository.delete(entity);
  }

  public void deleteAll() {
    contactJpaRepository.deleteAll();
  }
}
