package com.ntouzidis.crm2022.module.common.constants;

import com.ntouzidis.crm2022.module.common.enumeration.Role;

public class AuthorizationConstants {

  private static final String HAS_AUTHORITY = "hasAuthority('";

  private static final String HAS_ANY_AUTHORITY = "hasAnyAuthority('";

  public static final String ADMIN = HAS_AUTHORITY + Role.Constants.ADMIN +"')";

  public static final String ROOT = HAS_AUTHORITY + Role.Constants.ROOT + "')";

  public static final String ADMIN_OR_ROOT = HAS_ANY_AUTHORITY + Role.Constants.ADMIN + "', '" + Role.Constants.ROOT + "')";


  private AuthorizationConstants() {
  }
}
