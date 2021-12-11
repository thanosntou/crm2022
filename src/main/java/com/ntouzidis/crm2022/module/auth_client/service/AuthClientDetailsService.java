package com.ntouzidis.crm2022.module.auth_client.service;

import com.ntouzidis.crm2022.module.auth_client.entity.AuthClient;
import com.ntouzidis.crm2022.module.auth_client.entity.AuthClientScope;
import com.ntouzidis.crm2022.module.auth_client.entity.AuthGrantType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthClientDetailsService implements ClientDetailsService {

  private final AuthClientService authClientService;

  @Override
  public ClientDetails loadClientByClientId(String clientId) {
    AuthClient authClient =
        authClientService
            .findByClientId(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

    List<String> grantTypes =
        authClientService.findGrantTypesByAuthClientId(authClient.getId()).stream()
            .map(AuthGrantType::getName)
            .toList();

    List<String> scopes =
        authClientService.findScopesByAuthClientId(authClient.getId()).stream()
            .map(AuthClientScope::getName)
            .toList();

    List<GrantedAuthority> grantAuthorities =
        authClientService.findAuthoritiesByAuthClientId(authClient.getId()).stream()
            .map(auth -> (GrantedAuthority) new SimpleGrantedAuthority(auth.getName()))
            .toList();

    var clientDetails = new BaseClientDetails();
    clientDetails.setClientId(clientId);
    clientDetails.setClientSecret(authClient.getSecret());
    clientDetails.setAuthorizedGrantTypes(grantTypes);
    clientDetails.setScope(scopes);
    clientDetails.setAuthorities(grantAuthorities);
    return clientDetails;
  }
}
