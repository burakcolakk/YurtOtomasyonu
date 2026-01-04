package com.yurt.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AllocationRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long bedId;

    private LocalDate startDate;
    private LocalDate endDate;
}