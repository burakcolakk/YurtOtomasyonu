package com.yurt.project.dto.response;

import lombok.Data;

@Data
public class StaffResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String jobTitle;    // Görevi (Temizlik, Güvenlik, Müdür Yrd. vb.)
    private String phoneNumber;
}