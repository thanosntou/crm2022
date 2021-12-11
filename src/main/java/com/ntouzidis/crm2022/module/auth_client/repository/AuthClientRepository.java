package com.ntouzidis.crm2022.module.auth_client.repository;

import com.ntouzidis.crm2022.module.auth_client.entity.AuthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthClientRepository extends JpaRepository<AuthClient, Long> {

  Optional<AuthClient> findByClientId(String clientId);
}
