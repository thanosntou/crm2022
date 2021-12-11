package com.ntouzidis.crm2022.module.common.enumeration;

// Used enumeration for the roles to be able to stream them
public enum Role {

  ROOT(Constants.ROOT),
  ADMIN(Constants.ADMIN);

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  // This class helps so i can define another static fields using these roles
  // like AuthorizationConstants class
  public static class Constants {
    public static final String ROOT = "ROOT";
    public static final String ADMIN = "ADMIN";
  }

}
