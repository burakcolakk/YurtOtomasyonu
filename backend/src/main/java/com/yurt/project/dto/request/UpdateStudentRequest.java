package com.yurt.project.dto.request;

import lombok.Data;

@Data
public class UpdateStudentRequest {
    // Güncellemede tüm alanlar zorunlu olmak zorunda değil,
    // o yüzden Validation (@NotBlank vb.) eklemiyoruz.
    // Sadece değişenleri gönderebiliriz.

    private String firstName;
    private String lastName;
    private String phoneNumber; // Testin aradığı alan bu
    private String email;
    private String parentName;
    private String parentPhone;
    private String universityDepartment;

    private Long roomId; // Oda değişikliği yapmak isterse
}