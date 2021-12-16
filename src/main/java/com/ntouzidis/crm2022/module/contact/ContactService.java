package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.utils.ExcelHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
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
  public Contact createOne(@NonNull ContactForm form) {
    return contactRepository.saveNew(Contact.create(form));
  }

  @Transactional
  public List<Contact> createMultiple(@NonNull MultipartFile file) {
    String fileName = Objects.requireNonNull(file.getOriginalFilename());

    if (!ExcelHelper.isXlsx(fileName)) {
      throw new RuntimeException("Only [.xlsx] file type is supported now");
    }
    var sheet = ExcelHelper.getFirstSheet(file);

    var contacts =
        ExcelHelper.getContactRows(sheet).stream()
            .map(
                row -> {
                  var listOfValues = ExcelHelper.getContactRowValues(row);
                  return Contact.create(
                      (String) listOfValues.get(0),
                      (String) listOfValues.get(1),
                      (String) listOfValues.get(2),
                      (String) listOfValues.get(3),
                      (String) listOfValues.get(4),
                      (String) listOfValues.get(5),
                      (Long) listOfValues.get(6),
                      (Long) listOfValues.get(7),
                      (String) listOfValues.get(8),
                      (String) listOfValues.get(9),
                      (String) listOfValues.get(10),
                      (String) listOfValues.get(11));
                })
            .toList();

    contacts.forEach(contactRepository::saveNew);

    return contacts;
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
