package com.ntouzidis.crm2022.module.common.utils;

import com.ntouzidis.crm2022.module.common.dto.UserDTO;
import com.ntouzidis.crm2022.module.common.enumeration.Role;
import com.ntouzidis.crm2022.module.user.entity.Authority;
import com.ntouzidis.crm2022.module.user.entity.User;

import static java.util.stream.Collectors.toSet;

public class UserUtils {

  public static boolean isAdmin(User user) {
    return user.getAuthorities().stream().anyMatch(i -> i.getAuthority().equals(Role.ADMIN.name()));
  }

  public static boolean isRoot(User user) {
    return user.getAuthorities().stream().anyMatch(i -> i.getAuthority().equals(Role.ROOT.name()));
  }

  public static UserDTO toDTO(User user, boolean withAuthorities) {
    if (user == null) return null;

    return UserDTO.builder()
        .id(user.getId())
        .tenant(user.getTenant())
        .username(user.getUsername())
        .password(user.getPassword())
        .email(user.getEmail())
        .enabled(user.getEnabled())
        .createdOn(user.getCreatedOn())
        .authorities(withAuthorities ? user.getAuthorities()
            .stream()
            .map(Authority::getAuthority)
            .collect(toSet()) : null)
        .build();
  }

  public static UserDTO toDTOForFollower(User user) {
    if (user == null) return null;

    return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .build();
  }

  public static UserDTO toDTOForAdmin(User user) {
    if (user == null) return null;

    return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .enabled(user.getEnabled())
        .createdOn(user.getCreatedOn())
        .build();
  }

  public static UserDTO toDTOForRoot(User user) {
    if (user == null) return null;

    return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .enabled(user.getEnabled())
            .createdOn(user.getCreatedOn())
            .authorities(user.getAuthorities().stream().map(Authority::getAuthority).collect(toSet()))
            .tenant(user.getTenant())
            .build();
  }

  private UserUtils() {
  }
}
