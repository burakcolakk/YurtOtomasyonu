package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room extends BaseEntity {

    @Column(nullable = false)
    private Long id;
    private String roomNumber; // Örn: 101, 204

    private Integer floorNumber;

    private Integer capacity; // 2 kişilik, 4 kişilik


    // --- EKLENEN KISIM BAŞLANGIÇ ---
    // Bu alanı unutmuştuk, hata bundan kaynaklanıyor.
    // Varsayılan olarak 0 olsun (null olmasın).
    @Column(columnDefinition = "integer default 0")
    private Integer currentOccupancy = 0;
    // --- EKLENEN KISIM BİTİŞ ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Building building;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Bed> beds;

}