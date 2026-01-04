package com.yurt.project.dto.response;

import lombok.Data;

@Data
public class BedResponse {
    private Long id;
    private String bedName;
    private boolean isOccupied;
}