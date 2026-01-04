package com.yurt.project.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "buildings")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Building extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // Örn: A Blok, B Blok

    private String address;

    private Integer totalFloors;

    // Bir binada çok oda olur
    @JsonManagedReference
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Room> rooms;

}