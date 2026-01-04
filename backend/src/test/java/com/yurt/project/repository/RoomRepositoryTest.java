package com.yurt.project.repository;

import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomRepositoryTest {

    @Autowired private RoomRepository roomRepository;
    @Autowired private BuildingRepository buildingRepository;

    @Test
    void saveRoom_WithBuilding_ShouldWork() {
        // 1. Binayı Oluştur
        Building building = new Building();
        building.setName("B Blok");
        building.setTotalFloors(3);
        buildingRepository.save(building);

        // 2. Odayı Oluştur ve Binaya Bağla
        Room room = new Room();
        room.setRoomNumber("101");
        room.setCapacity(3);
        room.setFloorNumber(1);
        room.setBuilding(building); // İlişki kuruluyor

        roomRepository.save(room);

        // 3. Odayı Numarasından Bul
        Optional<Room> foundRoom = roomRepository.findByRoomNumber("101");

        // 4. KONTROL
        assertTrue(foundRoom.isPresent());
        assertEquals("B Blok", foundRoom.get().getBuilding().getName()); // İlişki üzerinden binaya eriştik mi?
    }
}