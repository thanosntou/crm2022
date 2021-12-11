package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.user.entity.Tenant;
import java.util.List;

public interface TenantService {

  Tenant getOne(Long id);

  Tenant getOne(String name);

  List<Tenant> getAll();

  Tenant create(String name);

  Tenant delete(Long id);
}
