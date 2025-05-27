package com.examly.springappuser.service;

import com.examly.springappuser.model.User;
import com.examly.springappuser.model.LoginDTO;
import com.examly.springappuser.repository.UserRepo;
import com.examly.springappuser.config.JwtUtils;
import com.examly.springappuser.config.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void createUser(User user) {
        userRepo.save(user);
    }

    @Override
    public LoginDTO loginUser(User user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        String token = jwtUtils.generateJwtToken(userDetails);

        LoginDTO dto = new LoginDTO();
        dto.setToken(token);
        dto.setUserId(userDetails.getUserId());
        dto.setUsername(userDetails.getUsername());
        dto.setUserRole(userDetails.getUserRole());
        return dto;
    }
}
