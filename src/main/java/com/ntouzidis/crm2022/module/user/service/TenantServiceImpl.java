package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ntouzidis.crm2022.module.common.constants.MessagesConstants.TENANT_NOT_FOUND_BY_ID;
import static com.ntouzidis.crm2022.module.common.constants.MessagesConstants.TENANT_NOT_FOUND_BY_NAME;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;

  public Tenant getOne(Long id) {
    return tenantRepository.findById(id).orElseThrow(() -> new NotFoundException(TENANT_NOT_FOUND_BY_ID));
  }

  public Tenant getOne(String name) {
    return tenantRepository.findByName(name).orElseThrow(() -> new NotFoundException(TENANT_NOT_FOUND_BY_NAME));
  }

  public List<Tenant> getAll() {
    return tenantRepository.findAll();
  }

  @Transactional
  public Tenant create(String name) {
    checkArgument(StringUtils.isNotBlank(name));
    Tenant tenant = new Tenant();
    tenant.setName(name);
    return tenantRepository.save(tenant);
  }

  @Transactional
  public Tenant delete(Long id) {
    checkArgument(id != null);
    Tenant tenant = getOne(id);
    tenantRepository.delete(tenant);
    return tenant;
  }
}
