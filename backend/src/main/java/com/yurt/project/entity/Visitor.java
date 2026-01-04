
package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "visitors")
@NoArgsConstructor
@AllArgsConstructor
public class Visitor extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student visitedStudent; // Kimi ziyarete geldi?

    private String visitorName;
    private String relation; // Annesi, Babası, Arkadaşı

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime entryTime; // Giriş Saati
    private LocalDateTime exitTime;  // Çıkış Saati
}