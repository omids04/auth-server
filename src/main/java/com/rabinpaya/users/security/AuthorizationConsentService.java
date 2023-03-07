package com.rabinpaya.users.security;

import com.rabinpaya.users.model.AuthorizationConsent;
import com.rabinpaya.users.repositories.AuthorizationConsentRepository;
import com.rabinpaya.users.repositories.ClientRepository;
import com.rabinpaya.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private final AuthorizationConsentRepository authorizationConsentRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        this.authorizationConsentRepository.save(toEntity(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        this.authorizationConsentRepository.deleteByClient_ClientIdAndUser_Username(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return this.authorizationConsentRepository
                .findByClient_ClientIdAndUser_Username(registeredClientId, principalName)
                .map(this::toObject).orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(AuthorizationConsent authorizationConsent) {
        var registeredClient = authorizationConsent.getClient();
        if (registeredClient == null) {
            throw new DataRetrievalFailureException("The RegisteredClient was not found in the RegisteredClientRepository.");
        }

        var builder = OAuth2AuthorizationConsent
                .withId(registeredClient.getClientId(), authorizationConsent.getUser().getUsername());

        if (authorizationConsent.getAuthorities() != null) {
            for (String authority : StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }

        return builder.build();
    }

    private AuthorizationConsent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
        var entity = new AuthorizationConsent();

        var user = userRepository.findByUsername(authorizationConsent.getPrincipalName());
        if (user.isEmpty())
            throw new DataRetrievalFailureException("Username %s does not exist".formatted(authorizationConsent.getPrincipalName()));

        var client = clientRepository.findByClientId(authorizationConsent.getRegisteredClientId());
        if (client.isEmpty())
            throw new DataRetrievalFailureException("ClientId %s does not exist".formatted(authorizationConsent.getRegisteredClientId()));

        entity.setClient(client.get());
        entity.setUser(user.get());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
