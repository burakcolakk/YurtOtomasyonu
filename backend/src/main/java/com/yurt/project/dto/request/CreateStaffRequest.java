package com.yurt.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateStaffRequest {

    @NotBlank(message = "Personel ismi boş olamaz")
    private String firstName;

    @NotBlank(message = "Personel soyismi boş olamaz")
    private String lastName;

    @NotBlank(message = "Görev (Ünvan) alanı boş olamaz")
    private String jobTitle; // Örn: Temizlik Görevlisi, Güvenlik, Müdür Yrd.

    @NotBlank(message = "Telefon numarası boş olamaz")
    private String phoneNumber;
}