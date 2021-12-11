package com.ntouzidis.crm2022.module.user.dto;

import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.provider.ClientDetails;

@Getter
@Setter
public class SetupDetailsDTO {

  private ClientDetails clientDetails;

  private Tenant tenant;

  private User user;
}
