package com.yurt.project.entity;

import com.yurt.project.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "leave_requests")
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 500)
    private String reason; // "Aile ziyareti" vb.

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING; // Varsayılan BEKLEMEDE
}