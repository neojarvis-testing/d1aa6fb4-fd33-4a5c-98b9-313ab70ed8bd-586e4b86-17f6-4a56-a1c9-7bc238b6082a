package com.examly.springappuser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginDTO {
    private String token;
    private String username;
    private String userRole;
    private Long userId;
}
