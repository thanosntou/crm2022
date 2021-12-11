package com.ntouzidis.crm2022.module.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public String[] getTest() {
    return new String[] { "Article 1", "Article 2", "Article 3" };
  }

  @GetMapping("/api/v1/test")
  public String[] getTestAuthenticated() {
    return new String[] { "Article 1", "Article 2", "Article 3" };
  }
}
