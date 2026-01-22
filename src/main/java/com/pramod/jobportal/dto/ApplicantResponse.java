package com.pramod.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicantResponse {

    private Long userId;
    private String name;
    private String email;

}
