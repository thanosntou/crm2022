package com.ntouzidis.crm2022.module.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailJpaRepository extends JpaRepository<EmailEntity, Long> {

  Optional<EmailEntity> findTopByOrderByEventIdDesc();
}
