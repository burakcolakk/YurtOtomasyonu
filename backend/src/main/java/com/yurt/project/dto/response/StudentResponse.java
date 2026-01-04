package com.yurt.project.dto.response;

import lombok.Data;

@Data
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String tcKimlikNo;
    private String universityDepartment;
    private String email;      // Testin aradığı alan
    private String roomNumber; // Hangi odada kaldığı
    private String phoneNumber;

    // Eğer öğrenci bir odada kalıyorsa, o odanın numarasını da buraya ekleyebiliriz
}