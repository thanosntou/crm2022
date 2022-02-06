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
