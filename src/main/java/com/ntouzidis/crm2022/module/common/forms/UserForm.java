package com.ntouzidis.crm2022.module.common.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntouzidis.crm2022.module.common.enumeration.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserForm {

  @NotNull
  @JsonProperty("role")
  private Role role;

  @NotBlank
  @JsonProperty("referer")
  private String referer;

  @NotBlank
  @JsonProperty("username")
  private String username;

  @NotBlank
  @JsonProperty("email")
  private String email;

  @NotBlank
  @JsonProperty("pass")
  private String pass;

  @NotBlank
  @JsonProperty("confirmPass")
  private String confirmPass;

  public UserForm() {
  }
}
