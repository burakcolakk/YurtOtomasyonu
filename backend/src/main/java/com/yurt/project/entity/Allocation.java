package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "allocations")
@NoArgsConstructor
@AllArgsConstructor
public class Allocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isActive = true; // Öğrenci hala yurtta mı?
}