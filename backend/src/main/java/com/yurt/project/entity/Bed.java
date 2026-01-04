package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // IMPORT

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "beds")
@NoArgsConstructor
@AllArgsConstructor
public class Bed extends BaseEntity {

    @Column(nullable = false)
    private String bedName; // Örn: 101-A, 101-B (Ranza alt/üst)

    private boolean isOccupied = false; // Dolu mu?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Room room;


}