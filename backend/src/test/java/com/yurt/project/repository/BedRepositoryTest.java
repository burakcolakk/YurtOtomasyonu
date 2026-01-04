package com.yurt.project.repository;

import com.yurt.project.entity.Bed;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BedRepositoryTest {

    @Autowired private BedRepository bedRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BuildingRepository buildingRepository;

    @Test
    void saveBed_ShouldLinkToRoom() {
        // Bina
        // DÜZELTİLMİŞ HALİ:
        Building building = new Building();
        building.setName("C Blok");
        building.setAddress("Adres");
        building.setTotalFloors(4);
        building = buildingRepository.save(building);

        // Oda
        Room room = new Room();
        room.setRoomNumber("205");
        room.setBuilding(building);
        room.setCapacity(2);
        roomRepository.save(room);

        // Yatak
        Bed bed = new Bed();
        bed.setBedName("205-A");
        bed.setOccupied(false);
        bed.setRoom(room);

        Bed savedBed = bedRepository.save(bed);

        // Kontrol
        assertNotNull(savedBed.getId());
        assertEquals("205", savedBed.getRoom().getRoomNumber());
    }
}