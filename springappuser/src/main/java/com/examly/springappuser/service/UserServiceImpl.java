package com.examly.springappuser.service;

import com.examly.springappuser.model.User;
import com.examly.springappuser.model.LoginDTO;
import com.examly.springappuser.repository.UserRepo;
import com.examly.springappuser.config.JwtUtils;
import com.examly.springappuser.config.UserPrinciple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

        return new LoginDTO(token, userDetails.getUsername(), userDetails.getUserRole(), userDetails.getUserId());
    }
}
