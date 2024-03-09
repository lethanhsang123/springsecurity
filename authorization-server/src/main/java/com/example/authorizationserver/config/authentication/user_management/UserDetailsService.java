package com.example.authorizationserver.config.authentication.user_management;

import com.example.authorizationserver.entities.Authority;
import com.example.authorizationserver.entities.User;
import com.example.authorizationserver.repositories.AuthorityRepository;
import com.example.authorizationserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
public class UserDetailsService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<UsernameNotFoundException> exS =
                () -> new UsernameNotFoundException(
                        "Problem during authentication!");
        User user = userRepository.findByEmail(username).orElseThrow(exS);
        List<String> authoritiesUser = new ArrayList<>();
        List<Authority> authorities = authorityRepository.findAllByUserId(user.getId());
        if (!authorities.isEmpty()) {
            authoritiesUser.addAll(authorities.stream().map(Authority::getName).toList());
        }
        return new SecurityUser(user.getEmail(), user.getHash(), user.getSalt(), authoritiesUser);
    }
}
