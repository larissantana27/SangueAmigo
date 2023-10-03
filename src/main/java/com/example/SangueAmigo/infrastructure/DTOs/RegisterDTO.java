package com.example.SangueAmigo.infrastructure.DTOs;

import com.example.SangueAmigo.model.user.AddressInformation;
import com.example.SangueAmigo.model.user.BiologicalInformation;
import com.example.SangueAmigo.model.user.User;

public record RegisterDTO (User user, BiologicalInformation biologicalInfo, AddressInformation addressInfo) {
}