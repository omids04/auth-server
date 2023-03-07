package com.rabinpaya.users.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "rabin_paya_role")
public class Role {

    @Id
    private UUID id;

    private String title;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @CreationTimestamp
    private Instant creationTimestamp;
}
