package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.common.pojo.Context;
import com.ntouzidis.crm2022.module.contact.utils.ExcelUtils;
import com.ntouzidis.crm2022.module.importfile.ImportService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ContactService {

  private final Context context;

  private final ContactRepository contactRepository;

  private final ImportService importService;

  @Transactional
  public Contact createOne(@NonNull ContactForm form) {
    return contactRepository.saveNew(Contact.create(form));
  }

  @Transactional
  public List<Contact> importFromFile(@NonNull MultipartFile file) {
    String fileName = Objects.requireNonNull(file.getOriginalFilename());

    if (!ExcelUtils.isXlsx(fileName)) {
      throw new RuntimeException("Only [.xlsx] file type is supported now");
    }
    var workbook = ExcelUtils.toWorkbook(file);
    var sheet = ExcelUtils.getFirstSheet(workbook);

    var contacts =
        ExcelUtils.getContactRows(sheet).stream()
            .map(
                row -> {
                  var listOfValues = ExcelUtils.getContactRowValues(row);
                  return Contact.create(
                      listOfValues.get(0),
                      listOfValues.get(1),
                      listOfValues.get(2),
                      listOfValues.get(3),
                      listOfValues.get(4),
                      listOfValues.get(5),
                      listOfValues.get(6),
                      listOfValues.get(7),
                      listOfValues.get(8),
                      listOfValues.get(9),
                      listOfValues.get(10),
                      listOfValues.get(11),
                      listOfValues.get(12));
                })
            .toList();

    contacts.forEach(contactRepository::saveNew);

    try {
      importService.createOne(context.getUser().getUsername(), file.getBytes(), contacts.size());

    } catch (IOException e) {
      throw new RuntimeException("Failed to get the bytes from MultipartFile");
    }
    return contacts;
  }

  @Transactional(readOnly = true)
  public List<Contact> getAll() {
    return contactRepository.getAll();
  }

  @Transactional(readOnly = true)
  public List<Contact> search(String company) {
    if (StringUtils.isBlank(company)) {
      return contactRepository.getAll();
    }
    return contactRepository.search(company);
  }

  @Transactional(readOnly = true)
  public Contact getOne(Long id) {
    return contactRepository
        .findOne(id)
        .orElseThrow(
            () -> new NotFoundException(String.format("Contact with id [%d] not found", id)));
  }

  @Transactional
  public void updateOne(@NonNull Long id, @NonNull EditContactForm form) {
    var contact = getOne(id);

    if (form.country() != null) {
      Country.validateName(form.country());
    }
    contactRepository.updateOne(
        id,
        Optional.ofNullable(form.company()).orElse(contact.company()),
        Optional.ofNullable(form.name()).orElse(contact.name()),
        Optional.ofNullable(form.surname()).orElse(contact.surname()),
        Optional.ofNullable(form.website()).orElse(contact.website()),
        Optional.ofNullable(form.email()).orElse(contact.email()),
        Optional.ofNullable(form.country()).orElse(contact.country().getName()),
        Optional.ofNullable(form.skype()).orElse(contact.skype()),
        Optional.ofNullable(form.viber()).orElse(contact.viber()),
        Optional.ofNullable(form.whatsApp()).orElse(contact.whatsApp()),
        Optional.ofNullable(form.weChat()).orElse(contact.weChat()),
        Optional.ofNullable(form.linkedIn()).orElse(contact.linkedIn()),
        Optional.ofNullable(form.businessType()).orElse(contact.businessType()),
        Optional.ofNullable(form.comments()).orElse(contact.comments()));
  }

  @Transactional
  public void delete(Long id) {
    contactRepository.delete(id);
  }

  @Transactional
  public void deleteAll() {
    contactRepository.deleteAll();
  }

  public List<Country> getSupportedCountries() {
    return Country.getSupportedCountries();
  }

  public List<BusinessType> getSupportedBusinessTypes() {
    return Arrays.stream(BusinessType.values()).toList();
  }
}
