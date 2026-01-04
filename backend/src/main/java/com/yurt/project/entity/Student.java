package com.yurt.project.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(nullable = false, unique = true, length = 11)
    private String tcKimlikNo;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String parentName;
    private String parentPhone;
    private String email;
    private String universityDepartment; // Örn: Bilgisayar Müh.
    private Integer studentNumber;

    // Login yapabilmesi için User ile bağlanıyoruz
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id") // Veritabanında room_id diye kolon açar
    private Room room;
}