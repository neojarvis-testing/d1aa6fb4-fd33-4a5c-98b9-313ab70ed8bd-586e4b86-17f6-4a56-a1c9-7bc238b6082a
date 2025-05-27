package com.examly.springappuser.service;

import com.examly.springappuser.model.User;
import com.examly.springappuser.model.LoginDTO;

public interface UserService {
    void createUser(User user);
    LoginDTO loginUser(User user);
}
