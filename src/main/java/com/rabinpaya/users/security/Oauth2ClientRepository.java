package com.rabinpaya.users.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabinpaya.users.model.Client;
import com.rabinpaya.users.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Oauth2ClientRepository implements RegisteredClientRepository {

    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.clientRepository.save(toEntity(registeredClient));
    }

    @Transactional
    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.clientRepository.findById(UUID.fromString(id)).map(this::toRegisteredClient).orElse(null);
    }

    @Transactional
    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.clientRepository.findByClientId(clientId).map(this::toRegisteredClient).orElse(null);
    }


    private RegisteredClient toRegisteredClient(Client client) {
        var clientAuthenticationMethods =  client.getClientAuthenticationMethods();

        var authorizationGrantTypes = client.getAuthorizationGrantTypes();

        var redirectUris = client.getRedirectUris();
        var clientScopes = client.getScopes();
        var builder = RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(methods -> clientAuthenticationMethods.forEach(method -> methods.add(resolveClientAuthenticationMethod(method))))
                .authorizationGrantTypes(grantTypes -> authorizationGrantTypes.forEach(type -> grantTypes.add(resolveAuthorizationGrantType(type))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));

//        var clientSettingsMap = parseMap(client.getClientSettings());
//        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

//        var tokenSettingsMap = parseMap(client.getTokenSettings());
//        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());
        return builder.build();
    }

    private Client toEntity(RegisteredClient registeredClient) {
        var clientAuthMethods = registeredClient
                .getClientAuthenticationMethods()
                .stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.toSet());

        var authorizationGrantTypes = registeredClient
                .getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.toSet());

        Client entity = new Client();
        entity.setId(UUID.fromString(registeredClient.getId()));
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(clientAuthMethods);
        entity.setAuthorizationGrantTypes(authorizationGrantTypes);
        entity.setRedirectUris(registeredClient.getRedirectUris());
        entity.setScopes(registeredClient.getScopes());
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ignored) {
            return Map.of();
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            return null;
        }
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        var code = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
        var credentials = AuthorizationGrantType.CLIENT_CREDENTIALS.getValue();
        var refresh = AuthorizationGrantType.REFRESH_TOKEN.getValue();

        if (code.equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (credentials.equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (refresh.equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {

        var basic = ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue();
        var post = ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue();
        var none = ClientAuthenticationMethod.NONE.getValue();

        if (basic.equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (post.equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (none.equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);      // Custom client authentication method
    }
}
