package com.ntouzidis.crm2022.module.user.service;

import com.google.common.collect.ImmutableSet;
import com.ntouzidis.crm2022.module.common.enumeration.Role;
import com.ntouzidis.crm2022.module.common.exceptions.NotFoundException;
import com.ntouzidis.crm2022.module.common.forms.AdminForm;
import com.ntouzidis.crm2022.module.common.forms.UserForm;
import com.ntouzidis.crm2022.module.common.forms.UserPasswordForm;
import com.ntouzidis.crm2022.module.common.forms.UserUpdateForm;
import com.ntouzidis.crm2022.module.common.pojo.Context;
import com.ntouzidis.crm2022.module.common.utils.UserUtils;
import com.ntouzidis.crm2022.module.user.entity.Authority;
import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.entity.User;
import com.ntouzidis.crm2022.module.user.repository.LoginRepository;
import com.ntouzidis.crm2022.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ntouzidis.crm2022.module.common.constants.MessagesConstants.*;
import static com.ntouzidis.crm2022.module.common.enumeration.Role.ADMIN;
import static com.ntouzidis.crm2022.module.common.utils.UserUtils.isAdmin;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Context context;
  private final UserRepository userRepository;
  private final LoginRepository loginRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> findOne(Long id) {
    return userRepository.findOne(id);
  }

  @Override
  public Optional<User> findOne(String username) {
    return userRepository.findOne(username);
  }

  @Override
  public Optional<User> findAdmin(String username) {
    return findOne(username).filter(UserUtils::isAdmin);
  }

  @Override
  public Optional<User> findOneGlobally(String username) {
    return userRepository.findOneGlobal(username);
  }

  @Override
  public User getOne(Long id) {
    return findOne(id).orElseThrow(() -> new NotFoundException(format(USER_NOT_FOUND, id)));
  }

  @Override
  public User getOne(String username) {
    return findOne(username).orElseThrow(() -> new NotFoundException(format(USER_NOT_FOUND, username)));
  }

  @Override
  public User getOneGlobally(String username) {
    return findOneGlobally(username).orElseThrow(() -> new NotFoundException(format(USER_NOT_FOUND, username)));
  }

  @Override
  public User getAdmin(Long id) {
    return findOne(id).filter(UserUtils::isAdmin).orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_BY_ID));
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  public List<User> getAllGlobal() {
    return userRepository.findAllGlobal();
  }

  @Override
  public List<User> getAdminsByTenant(Long tenantId) {
    return userRepository.findAllByTenant(tenantId)
        .stream()
        .filter(UserUtils::isAdmin)
        .collect(toList());
  }

  @Override
  public User signUp(UserForm form) {
    Role role = form.getRole();
    String username = form.getUsername();
    String email = form.getEmail();
    String pass = form.getPass();
    String confirmPass = form.getConfirmPass();
    String refName = form.getReferer();

    User referer = findOneGlobally(refName).orElseThrow(() ->
        new NotFoundException(format("Referer %s not found", refName)));

    checkArgument(isAdmin(referer),
        format("Referer %s not found", refName));

    checkArgument(pass.equals(confirmPass), PASSWORD_NOT_MATCH);

    return createUser(referer.getTenant(), username, pass, email, ImmutableSet.of(new Authority(role)));
  }

  @Override
  @Transactional
  public User createAdmin(Tenant tenant, AdminForm form) {
    String username = form.getUsername();
    String pass = form.getPass();
    String confirmPass = form.getConfirmPass();
    String email = form.getEmail();

    checkArgument(pass.equals(confirmPass), PASSWORD_NOT_MATCH);

    return createUser(tenant, username, pass, email, ImmutableSet.of(new Authority(ADMIN)));
  }

  public User createUser(Tenant tenant, String username, String pass, String email, Set<Authority> authorities) {
    checkArgument(!usernameExistsGlobally(username), "username exists");

    User user = new User(username, passwordEncoder.encode(pass));
    user.setTenant(tenant);
    user.setEmail(email);
    user.setEnabled(false);
    user.setAuthorities(new ArrayList<>());

    authorities.forEach(user::addAuthority);

    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User update(Long userId, UserUpdateForm form) {
      User user = getOne(userId);
      return userRepository.save(user);
  }

  @Override
  @Transactional
  public User changePassword(User user, UserPasswordForm form) {
    String newPass = form.getNewPass();
    String confirmPass = form.getConfirmPass();

    checkArgument(newPass.equals(confirmPass), "Wrong confirmation password");

    user.setPassword(passwordEncoder.encode(newPass));
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User delete(User user) {
    loginRepository.deleteAllByUser(user);
    return userRepository.delete(user);
  }


  private boolean usernameExistsGlobally(String username) {
    return userRepository.findOneGlobal(username).isPresent();
  }

}
