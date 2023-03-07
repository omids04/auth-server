package com.rabinpaya.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "rabin_paya_user")
public class User {

    @Id
    private UUID id;

    private String phone;

    private String username;

    @Transient
    private String otp;

    private String password;

    @OneToMany(mappedBy = "user")
    private Set<AuthorizationConsent> consents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @CreationTimestamp
    private Instant creationTimestamp;
}
