package com.yurt.project.dto.request;

import lombok.Data;

@Data
public class CreateBuildingRequest {
    private String name;
    private String address;
    private Integer totalFloors;
}