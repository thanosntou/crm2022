package com.ntouzidis.crm2022.module.api;

import com.ntouzidis.crm2022.module.common.pojo.Context;
import com.ntouzidis.crm2022.module.email.Email;
import com.ntouzidis.crm2022.module.email.EmailForm;
import com.ntouzidis.crm2022.module.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ntouzidis.crm2022.module.common.constants.AuthorizationConstants.ADMIN_OR_ROOT;
import static com.ntouzidis.crm2022.module.common.constants.ControllerPaths.EMAIL_CONTROLLER_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = EMAIL_CONTROLLER_PATH, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class EmailController {

  private final Context context;

  private final EmailService emailService;

  @PostMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public void sendAll(@RequestBody @Valid EmailForm form) {
    emailService.send(context.getUser(), form);
  }

  @GetMapping
  @PreAuthorize(ADMIN_OR_ROOT)
  public List<Email> getAll() {
    return emailService.getAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize(ADMIN_OR_ROOT)
  public Email getOne(@PathVariable Long id) {
    return emailService.getOne(id);
  }
}
