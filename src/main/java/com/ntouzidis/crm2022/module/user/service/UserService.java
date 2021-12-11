package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.common.forms.AdminForm;
import com.ntouzidis.crm2022.module.common.forms.UserForm;
import com.ntouzidis.crm2022.module.common.forms.UserPasswordForm;
import com.ntouzidis.crm2022.module.common.forms.UserUpdateForm;
import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

  Optional<User> findOne(Long id);

  Optional<User> findOne(String username);

  Optional<User> findAdmin(String username);

  Optional<User> findOneGlobally(String username);

  User getOne(Long id);

  User getOne(String username);

  User getOneGlobally(String username);

  User getAdmin(Long id);

  List<User> getAll();

  List<User> getAllGlobal();

  List<User> getAdminsByTenant(Long tenantId);

  User signUp(UserForm userForm);

  User createAdmin(Tenant tenant, AdminForm adminForm);

  User update(Long userId, UserUpdateForm userUpdateForm);

  User changePassword(User user, UserPasswordForm userPasswordForm);

  User delete(User user);

}
