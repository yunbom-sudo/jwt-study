package com.example.king.domain.auth.user.dto.response;

import com.example.king.domain.auth.user.entity.Role;
import com.example.king.domain.auth.user.entity.User;

public record SignUpResponse(
    Long id,
    String username,
    Role role
) {
    public SignUpResponse(User user){
        this(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
