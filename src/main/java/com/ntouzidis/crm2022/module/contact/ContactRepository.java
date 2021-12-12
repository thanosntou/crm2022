package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ContactRepository {

  private final ContactJpaRepository contactJpaRepository;

  public List<Contact> getAll() {
    return contactJpaRepository.findAll().stream().map(ContactEntity::toDomain).toList();
  }

  public Optional<Contact> findOne(Long id) {
    return contactJpaRepository.findById(id).map(ContactEntity::toDomain);
  }

  public Contact saveNew(Contact contact) {
    return contactJpaRepository.save(ContactEntity.fromDomain(contact)).toDomain();
  }

  public void delete(Long id) {
    var entity =
        contactJpaRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("Contact with id [%d] not found", id)));

    contactJpaRepository.delete(entity);
  }
}
