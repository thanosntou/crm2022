package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {

  private final ContactRepository contactRepository;

  @Transactional(readOnly = true)
  public List<Contact> getAll() {
    return contactRepository.getAll();
  }

  @Transactional(readOnly = true)
  public Contact getOne(Long id) {
    return contactRepository
        .findOne(id)
        .orElseThrow(
            () -> new NotFoundException(String.format("Contact with id [%d] not found", id)));
  }

  @Transactional
  public Contact create(ContactForm form) {
    return contactRepository.saveNew(Contact.create(form));
  }

  @Transactional
  public void delete(Long id) {
    contactRepository.delete(id);
  }

  public List<Country> getSupportedCountries() {
    return Country.getSupportedCountries();
  }

  public List<BusinessType> getSupportedBusinessTypes() {
    return Arrays.stream(BusinessType.values()).toList();
  }
}
