package com.yurt.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotNull
    private String roomNumber;

    @NotNull
    private Integer capacity;

    private Integer floorNumber;

    @NotNull(message = "Oda bir binaya ait olmalıdır")
    private Long buildingId; // Dikkat: Tüm bina objesini değil, sadece ID istiyoruz.
}