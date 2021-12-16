package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.contact.utils.ExcelGenerator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
@AllArgsConstructor
public class ContactService {

  private final ContactRepository contactRepository;

  @Transactional
  public Contact createOne(@NonNull ContactForm form) {
    return contactRepository.saveNew(Contact.create(form));
  }

  @Transactional
  public List<Contact> createMultiple(@NonNull MultipartFile file) {
    String fileName = Objects.requireNonNull(file.getOriginalFilename());

    if (!ExcelGenerator.isXlsx(fileName)) {
      throw new RuntimeException("Only [.xlsx] file type is supported now");
    }
    var sheet = ExcelGenerator.getFirstSheet(file);

    var contacts =
        ExcelGenerator.getContactRows(sheet).stream()
            .map(
                row -> {
                  var listOfValues = ExcelGenerator.getContactRowValues(row);
                  return Contact.create(
                      listOfValues.get(0),
                      listOfValues.get(1),
                      listOfValues.get(2),
                      listOfValues.get(3),
                      listOfValues.get(4),
                      listOfValues.get(5),
                      Long.valueOf(listOfValues.get(6)),
                      Long.valueOf(listOfValues.get(7)),
                      listOfValues.get(8),
                      listOfValues.get(9),
                      listOfValues.get(10),
                      listOfValues.get(11));
                })
            .toList();
    contacts.forEach(contactRepository::saveNew);
    return contacts;
  }

  public void exportContacts() {
    var contacts = contactRepository.getAll();

    ExcelGenerator.generate(contacts);
  }

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
