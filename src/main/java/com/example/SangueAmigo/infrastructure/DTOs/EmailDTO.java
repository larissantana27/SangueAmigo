package com.example.SangueAmigo.infrastructure.DTOs;

public record EmailDTO (String ownerRef, String emailFrom, String emailTo, String subject, String text){
}