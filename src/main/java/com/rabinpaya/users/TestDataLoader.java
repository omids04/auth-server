package com.rabinpaya.users;

import com.rabinpaya.users.model.Client;
import com.rabinpaya.users.model.Role;
import com.rabinpaya.users.model.User;
import com.rabinpaya.users.repositories.ClientRepository;
import com.rabinpaya.users.repositories.UserRepository;
import com.rabinpaya.users.services.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) throws Exception {
        String s = InetAddress.getLocalHost().getHostName();
        System.out.println(s);
        var role = new Role();
        role.setId(UUID.randomUUID());
        role.setTitle("Admin");
        roleRepository.save(role);

        var user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setPhone("092145855555");
        user1.setUsername("user1");
        user1.setRoles(Set.of(role));
        user1.setPassword(passwordEncoder.encode("user1"));

        var user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setPhone("092145855855");
        user2.setUsername("user2");
        user2.setRoles(Set.of(role));
        user2.setPassword(passwordEncoder.encode("user2"));

        userRepository.saveAll(List.of(user1, user2));

        var client = new Client();
        client.setId(UUID.randomUUID());
        client.setClientId("client");
        client.setClientName("client");
        client.setClientSecret(passwordEncoder.encode("client"));
        client.setClientIdIssuedAt(Instant.now());
        client.setRedirectUris(Set.of("https://google.com/auth"));
        client.setClientAuthenticationMethods(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        client.setAuthorizationGrantTypes(Set.of(AuthorizationGrantType.AUTHORIZATION_CODE.getValue(), AuthorizationGrantType.REFRESH_TOKEN.getValue()));
        client.setScopes(Set.of(OidcScopes.OPENID, OidcScopes.PROFILE));

        clientRepository.save(client);

    }
}
