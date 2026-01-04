package com.yurt.project.repository;

import com.yurt.project.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AllocationRepositoryTest {

    @Autowired private AllocationRepository allocationRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository; // Öğrenci için gerekli
    @Autowired private BedRepository bedRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BuildingRepository buildingRepository; // Oda için gerekli

    @Test
    void existsByStudent_IdAndIsActiveTrue_ShouldReturnTrue() {
        // --- 1. ALTYAPIYI KUR (Bina -> Oda -> Yatak) ---

        // Önce Binayı kaydet
        Building building = new Building();
        building.setName("Test Yurdu - Blok A");
        building.setAddress("Kampüs İçi");
        building.setTotalFloors(4);
        buildingRepository.save(building);

        // Odayı kaydet (Binaya bağlı)
        Room room = new Room();
        room.setRoomNumber("999");
        room.setBuilding(building);
        room.setCapacity(2);
        roomRepository.save(room);

        // Yatağı kaydet (Odaya bağlı)
        Bed bed = new Bed();
        bed.setBedName("999-A");
        bed.setRoom(room);
        bed.setOccupied(false);
        bedRepository.save(bed);

        // --- 2. KULLANICI VE ÖĞRENCİYİ KUR ---

        // Önce User kaydet (Öğrenci buna bağlı)
        User user = new User();
        user.setUsername("testallocationuser");
        user.setPassword("password123");
        user.setEmail("allocation@test.com");
        userRepository.save(user);

        // Öğrenciyi kaydet
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("Öğrenci");
        student.setTcKimlikNo("10000000001"); // Unique olmalı
        student.setUser(user);
        Student savedStudent = studentRepository.save(student);

        // --- 3. YERLEŞİMİ YAP (ALLOCATION) ---

        Allocation allocation = new Allocation();
        allocation.setStudent(savedStudent);
        allocation.setBed(bed);
        allocation.setStartDate(LocalDate.now());
        allocation.setActive(true); // <--- KRİTİK NOKTA: Aktif olarak işaretledik
        allocationRepository.save(allocation);

        // --- 4. TEST ET ---
        // "Bu öğrenci şu an aktif olarak bir odada kalıyor mu?"
        boolean exists = allocationRepository.existsByStudent_IdAndIsActiveTrue(savedStudent.getId());

        // --- 5. SONUÇ ---
        assertTrue(exists, "Öğrenci aktif yerleşime sahip olduğu için TRUE dönmeliydi.");
    }
}