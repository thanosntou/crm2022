package com.ntouzidis.crm2022.module.contact;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactJpaRepository extends JpaRepository<ContactEntity, Long> {

  List<ContactEntity> findAllByBusinessType(BusinessType businessType);

  List<ContactEntity> findByCompanyStartsWith(String company);
}
