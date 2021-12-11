package com.ntouzidis.crm2022.module.auth_client.repository;

import com.ntouzidis.crm2022.module.auth_client.entity.AuthClientAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthClientAuthorityRepository extends JpaRepository<AuthClientAuthority, Long> {

  List<AuthClientAuthority> findByClientId(Long clientId);
}
