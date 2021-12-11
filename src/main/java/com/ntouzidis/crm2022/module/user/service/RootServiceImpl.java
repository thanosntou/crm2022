package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.entity.User;
import com.ntouzidis.crm2022.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RootServiceImpl implements RootService {

  private final UserService userService;
  private final UserRepository userRepository;
  private final TenantService tenantService;

  @Override
  public User deleteAdminUser(User admin, Long id) {
    User adminUser = Optional.ofNullable(admin)
        .orElseGet(() -> userService.getAdmin(id));
    return userService.delete(adminUser);
  }

  @Override
  public Tenant deleteTenant(Long tenantId) {
    userService.getAdminsByTenant(tenantId)
        .forEach(admin -> deleteAdminUser(admin, null));
    return tenantService.delete(tenantId);
  }
}
