package com.example.springsecurity.config.approach_2.authentication.manage_users;

import com.example.springsecurity.entity.Authority;
import com.example.springsecurity.entity.Role;
import com.example.springsecurity.entity.User;
import com.example.springsecurity.repository.AuthorityRepository;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceCustom implements UserDetailsManager {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public UserDetailsServiceCustom(UserRepository userRepository, RoleRepository roleRepository,
                                    AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

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
        List<Role> roles = roleRepository.findAllByUserId(user.getId());
        List<Authority> authorities = authorityRepository.findAllByUserId(user.getId());
        if (!roles.isEmpty()) {
            authoritiesUser.addAll(roles.stream().map(Role::getName).toList());
        }
        if (!authorities.isEmpty()) {
            authoritiesUser.addAll(authorities.stream().map(Authority::getName).toList());
        }
        return new SecurityUser(user.getEmail(), user.getHash(), user.getSalt(), authoritiesUser);
    }
}
