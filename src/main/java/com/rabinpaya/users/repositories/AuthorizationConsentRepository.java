package com.rabinpaya.users.repositories;

import com.rabinpaya.users.model.AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorizationConsentRepository extends JpaRepository<AuthorizationConsent, UUID> {
    Optional<AuthorizationConsent> findByClient_ClientIdAndUser_Username(String clientId, String username);
    void deleteByClient_ClientIdAndUser_Username(String clientId, String username);
}
