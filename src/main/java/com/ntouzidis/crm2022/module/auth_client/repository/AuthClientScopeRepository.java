package com.ntouzidis.crm2022.module.auth_client.repository;

import com.ntouzidis.crm2022.module.auth_client.entity.AuthClientScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthClientScopeRepository extends JpaRepository<AuthClientScope, Long> {

  List<AuthClientScope> findByClientId(Long clientId);
}
