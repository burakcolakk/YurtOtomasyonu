package com.yurt.project.dto.request;

import lombok.Data;

@Data
public class UpdateStaffRequest {
    private String firstName;
    private String lastName;
    private String jobTitle;    // Görevi (Temizlik, Güvenlik vb.)
    private String phoneNumber;
}