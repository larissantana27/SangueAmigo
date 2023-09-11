package com.example.SangueAmigo.model.user.authentication;

public enum UserRole {
    ADMIN("admin"), USER("user");

    private final String role;

    UserRole(String role){
        this.role = role;
    }

    public String role(){
        return role;
    }
}
