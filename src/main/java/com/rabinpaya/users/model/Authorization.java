package com.rabinpaya.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "oauth2_authorization")
public class Authorization {
    @Id
    @Column
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    @ElementCollection
    @CollectionTable(name="authorized_scope", joinColumns=@JoinColumn(name="id"))
    @Column(name="name")
    private Set<String> authorizedScopes;
    @Column(length = 4000)
    private String attributes;
    @Column(length = 500, name = "auth_state")
    private String state;

    @Column(length = 4000)
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private String authorizationCodeMetadata;

    @Column(length = 4000)
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    @Column(length = 2000)
    private String accessTokenMetadata;
    private String accessTokenType;
    @Column(length = 1000)
    private String accessTokenScopes;

    @Column(length = 4000)
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    @Column(length = 2000)
    private String refreshTokenMetadata;

    @Column(length = 4000)
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    @Column(length = 2000)
    private String oidcIdTokenMetadata;
    @Column(length = 2000)
    private String oidcIdTokenClaims;

}