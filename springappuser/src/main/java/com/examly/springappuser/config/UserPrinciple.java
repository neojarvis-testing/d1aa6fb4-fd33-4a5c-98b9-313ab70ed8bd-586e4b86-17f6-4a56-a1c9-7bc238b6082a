package com.examly.springappuser.config;

import com.examly.springappuser.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;
import org.springframework.security.core.*;

public class UserPrinciple implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private String userRole;

    public UserPrinciple(User user) {
        this.userId = user.getUserId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserRole() {
        return userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + userRole);
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
