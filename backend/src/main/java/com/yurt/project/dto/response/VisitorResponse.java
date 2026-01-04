package com.yurt.project.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitorResponse {
    private Long id;
    private String visitorName;
    private String studentName;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}