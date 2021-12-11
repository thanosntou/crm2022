package com.ntouzidis.crm2022.module.common.constants;

import com.ntouzidis.crm2022.module.common.enumeration.Role;

public class AuthorizationConstants {

  private static final String HAS_AUTHORITY = "hasAuthority('";

  public static final String ADMIN = HAS_AUTHORITY + Role.Constants.ADMIN +"')";

  public static final String ROOT = HAS_AUTHORITY + Role.Constants.ROOT + "')";


  private AuthorizationConstants() {
  }
}
