package com.example.SangueAmigo.infrastructure.DTOs;

import com.example.SangueAmigo.model.user.authentication.UserRole;

public record RegisterDTO (String firstname, String lastname, String email, String password, UserRole role){
}