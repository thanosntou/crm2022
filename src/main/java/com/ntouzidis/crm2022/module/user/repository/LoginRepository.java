package com.ntouzidis.crm2022.module.user.repository;

import com.ntouzidis.crm2022.module.user.entity.Login;
import com.ntouzidis.crm2022.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginRepository extends JpaRepository<Login, Long> {

  List<Login> findAllByTenantId(Long tenantId);

  void deleteAllByUser(User user);
}
