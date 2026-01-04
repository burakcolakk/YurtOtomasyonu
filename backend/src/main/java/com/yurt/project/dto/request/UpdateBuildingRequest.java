package com.yurt.project.dto.request;

import lombok.Data;

@Data
public class UpdateBuildingRequest {
    // Güncellenebilecek alanlar (Create ile aynı olabilir)
    private String name;
    private String address;
    private Integer totalFloors;
}