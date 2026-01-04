package com.yurt.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateVisitorRequest {

    @NotBlank(message = "Ziyaretçi ismi boş olamaz")
    private String visitorName;

    @NotBlank(message = "Ziyaret edilen öğrenci ismi boş olamaz")
    private String studentName;

    @NotBlank(message = "Yakınlık derecesi (Baba, Arkadaş vb.) boş olamaz")
    private String relation;
}