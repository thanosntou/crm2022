package com.ntouzidis.crm2022.module.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntouzidis.crm2022.module.user.entity.Tenant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@JsonInclude(NON_NULL)
public class UserDTO implements Serializable {

  @JsonProperty
  private Long id;

  @JsonProperty
  private Tenant tenant;

  @JsonProperty
  private String username;

  @JsonProperty
  private String password;

  @JsonProperty
  private Set<String> authorities;

  @JsonProperty
  private String email;

  @JsonProperty
  private Boolean enabled;

  @JsonProperty
  private LocalDateTime createdOn;
}
