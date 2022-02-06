package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactJpaRepository extends JpaRepository<ContactEntity, Long> {

  List<ContactEntity> findAllByBusinessType(BusinessType businessType);

  List<ContactEntity> findByCompanyStartsWithIgnoreCase(String company);

  @Query("SELECT c FROM ContactEntity c " +
      "WHERE LOWER(c.company) LIKE %:value% " +
      "OR LOWER(c.name) LIKE %:value% " +
      "OR LOWER(c.surname) LIKE %:value% " +
      "OR LOWER(c.website) LIKE %:value% " +
      "OR LOWER(c.email) LIKE %:value% " +
      "OR LOWER(c.country) LIKE %:value% " +
      "OR LOWER(c.skype) LIKE %:value% " +
      "OR LOWER(c.viber) LIKE %:value% " +
      "OR LOWER(c.whatsApp) LIKE %:value% " +
      "OR LOWER(c.weChat) LIKE %:value% " +
      "OR LOWER(c.linkedIn) LIKE %:value% " +
      "OR LOWER(c.businessType) LIKE %:value% " +
      "OR LOWER(c.comments) LIKE %:value%")
  List<ContactEntity> searchByValue(@Param("value") String value);
}
