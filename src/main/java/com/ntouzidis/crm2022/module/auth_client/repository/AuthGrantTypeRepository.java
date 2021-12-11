package com.ntouzidis.crm2022.module.auth_client.repository;

import com.ntouzidis.crm2022.module.auth_client.entity.AuthGrantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthGrantTypeRepository extends JpaRepository<AuthGrantType, Long> {

  List<AuthGrantType> findByClientId(Long authClientId);
}
