package com.ntouzidis.crm2022.module.user.repository;

import com.ntouzidis.crm2022.module.user.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

  List<Authority> findAllByUsername(String username);

}
