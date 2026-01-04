package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "staff")
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends BaseEntity {

    private String firstName;
    private String lastName;
    private String jobTitle; // Güvenlik, Temizlikçi, Müdür
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Sisteme giriş yapacaksa
}