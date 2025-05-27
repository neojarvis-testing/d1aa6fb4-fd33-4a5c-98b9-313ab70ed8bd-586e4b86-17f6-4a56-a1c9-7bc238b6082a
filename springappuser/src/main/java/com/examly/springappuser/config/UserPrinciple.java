package com.examly.springappuser.config;

import com.examly.springappuser.model.User;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.*;

import java.util.*;

@Getter
public class UserPrinciple implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String userRole;

    public UserPrinciple(User user) {
        this.userId = user.getUserId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + userRole);
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
