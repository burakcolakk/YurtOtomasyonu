package com.yurt.project.integration;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.service.StudentService;
import com.yurt.project.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class RoomCapacityIntegrationTest {

    @Autowired private StudentService studentService;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BuildingRepository buildingRepository;

    @Test
    void shouldThrowException_WhenRoomIsFull() {
        // 1. Bina Oluştur (DÜZELTİLEN KISIM BURASI)
        Building building = new Building();
        building.setName("Kapasite Test Bloğu"); // <-- Artık isim veriyoruz!
        building.setAddress("Kampüs İçi");
        building.setTotalFloors(5);
        building = buildingRepository.save(building); // Hata burada çözüldü ✅

        // 2. 1 Kişilik Oda Oluştur
        Room room = new Room();
        room.setRoomNumber("101-Dolu");
        room.setCapacity(1); // KAPASİTE 1
        room.setCurrentOccupancy(1); // ZATEN 1 KİŞİ VAR
        room.setBuilding(building);
        room.setFloorNumber(1);
        room = roomRepository.save(room);

        // 3. Yeni Öğrenci Kaydı Hazırla
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstName("Fazlalık");
        request.setLastName("Öğrenci");
        request.setEmail("fazla@test.com");
        request.setTcKimlikNo("12345678901");
        request.setRoomId(room.getId()); // Dolu odayı seçtik

        // 4. Hata Fırlatmasını Bekle
        assertThrows(BusinessException.class, () -> {
            studentService.createStudent(request);
        }, "Dolu odaya kayıt yapıldığında BusinessException fırlatılmalı!");
    }
}