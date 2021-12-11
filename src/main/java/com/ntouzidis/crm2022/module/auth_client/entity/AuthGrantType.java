package com.ntouzidis.crm2022.module.auth_client.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auth_client_grant_type")
public class AuthGrantType implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "client_id")
  private Long clientId;

  @Column(name = "name")
  private String name;

  public AuthGrantType(Long clientId, String name) {
    this.clientId = clientId;
    this.name = name;
  }
}
