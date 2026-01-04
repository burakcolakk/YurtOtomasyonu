package com.yurt.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStudentRequest {

    @NotBlank(message = "TC Kimlik Numarası boş olamaz")
    @Size(min = 11, max = 11, message = "TC Kimlik 11 haneli olmalıdır")
    private String tcKimlikNo;

    @NotBlank(message = "Ad boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    private String lastName;

    private String phoneNumber;

    // Veli bilgileri
    private String parentName;
    private String parentPhone;

    private String universityDepartment;

    @NotBlank(message = "Email boş olamaz") // Eksik olan alan buydu
    private String email;

    @NotNull(message = "Oda seçimi zorunludur") // Eksik olan alan buydu
    private Long roomId;
}
