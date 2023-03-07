package com.rabinpaya.users.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Client {
    @Id
    private UUID id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;

    @ElementCollection
    @CollectionTable(name="auth_method", joinColumns=@JoinColumn(name="id"))
    @Column(name="name")
    private Set<String> clientAuthenticationMethods;
    @ElementCollection
    @CollectionTable(name="grant_type", joinColumns=@JoinColumn(name="id"))
    @Column(name="name")
    private Set<String> authorizationGrantTypes;
    @ElementCollection
    @CollectionTable(name="redirect_uri", joinColumns=@JoinColumn(name="id"))
    @Column(name="uri")
    private Set<String> redirectUris;
    @ElementCollection
    @CollectionTable(name="scope", joinColumns=@JoinColumn(name="id"))
    @Column(name="name")
    private Set<String> scopes;
    @Column(length = 2000)
    private String clientSettings;
    @Column(length = 2000)
    private String tokenSettings;

    @OneToMany(mappedBy = "client")
    private Set<AuthorizationConsent> consents;

}