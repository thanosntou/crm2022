package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.common.dto.UserDTO;
import com.ntouzidis.crm2022.module.common.forms.UserForm;
import com.ntouzidis.crm2022.module.common.forms.UserPasswordForm;
import com.ntouzidis.crm2022.module.common.pojo.Context;
import com.ntouzidis.crm2022.module.user.entity.CustomUserDetails;
import com.ntouzidis.crm2022.module.user.entity.User;
import com.ntouzidis.crm2022.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ntouzidis.crm2022.module.common.constants.ControllerPaths.USER_CONTROLLER_PATH;
import static com.ntouzidis.crm2022.module.common.utils.UserUtils.toDTO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(
    value = USER_CONTROLLER_PATH,
    produces = APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class UserController {

  private final Context context;
  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDTO> signUp(@RequestBody @Valid UserForm userForm) {
    User user = userService.signUp(userForm);

    // setting the non encrypted pass for the ui,
    // to login immediately after creation
    user.setPassword(userForm.getPass());
    return ok(toDTO(user, false));
  }

  @PutMapping("/pass")
  public ResponseEntity<UserDTO> changePersonalPassword(@RequestBody @Valid UserPasswordForm userDTOPassForm) {
    return ok(toDTO(userService.changePassword(context.getUser(), userDTOPassForm), false));
  }

  @GetMapping("/authenticate")
  public ResponseEntity<CustomUserDetails> authenticate() {
    return ok(context.getUserDetails());
  }
}
