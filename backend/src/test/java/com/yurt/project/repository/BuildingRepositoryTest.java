package com.yurt.project.repository;

import com.yurt.project.entity.Building;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BuildingRepositoryTest {

    @Autowired private BuildingRepository buildingRepository;

    @Test
    void saveAndFindBuilding_ShouldWork() {
        // 1. HAZIRLIK
        Building building = new Building();
        building.setName("A Blok");
        building.setAddress("Merkez Kampüs");
        building.setTotalFloors(5);

        // 2. EYLEM
        Building savedBuilding = buildingRepository.save(building);

        // ID atandı mı?
        assertNotNull(savedBuilding.getId());

        // Veritabanından geri çek
        Optional<Building> found = buildingRepository.findById(savedBuilding.getId());

        // 3. KONTROL
        assertTrue(found.isPresent());
        assertEquals("A Blok", found.get().getName());
    }
}