package com.pramod.jobportal.dto;

import com.pramod.jobportal.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private Role role;
}