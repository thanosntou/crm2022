package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.user.entity.CustomUserDetails;
import com.ntouzidis.crm2022.module.user.entity.User;
import com.ntouzidis.crm2022.module.user.repository.AuthorityRepository;
import com.ntouzidis.crm2022.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  private final AuthorityRepository authorityRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username) {
    User user =
        userRepository
            .findOneGlobal(username)
            .orElseThrow(
                () -> new NotFoundException(String.format("Username [%s] not found", username)));

    if (CollectionUtils.isEmpty(user.getAuthorities())) {
      var authorities = authorityRepository.findAllByUsername(user.getUsername());
      //      user.setAuthorities(authorities);
      authorities.forEach(user::addAuthority);
    }

    CustomUserDetails customUserDetail = new CustomUserDetails();
    customUserDetail.setUser(user);

    return customUserDetail;
  }
}
