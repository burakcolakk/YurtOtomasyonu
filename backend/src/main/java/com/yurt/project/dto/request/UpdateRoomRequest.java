package com.yurt.project.dto.request;

import lombok.Data;

@Data
public class UpdateRoomRequest {
    private String roomNumber;
    private Integer capacity;
    private Integer floorNumber;
    // Bina değişikliği genellikle update ile yapılmaz, zor olur, o yüzden binaId koymuyoruz.
}