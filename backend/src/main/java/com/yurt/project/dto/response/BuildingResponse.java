package com.yurt.project.dto.response;

import lombok.Data;

@Data
public class BuildingResponse {
    private Long id;
    private String name;
    private String address;
    private Integer totalFloors;
}