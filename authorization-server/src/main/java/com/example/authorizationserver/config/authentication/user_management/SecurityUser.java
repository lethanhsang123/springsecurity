package com.example.authorizationserver.config.authentication.user_management;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
    private final String username;
    private final String hash;
    private final String salt;
    private final List<String> authorities;

    public SecurityUser(String username, String hash, String salt, List<String> authorities) {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.hash;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getSalt() { return this.salt; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
