package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.UpdateBuildingRequest; // Bu DTO'yu oluşturmayı unutma
import com.yurt.project.entity.Building;
import com.yurt.project.repository.BuildingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BuildingCrudIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BuildingRepository buildingRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAndDeleteBuilding_ShouldWork() throws Exception {
        // 1. HAZIRLIK: Veritabanına bir bina ekle
        Building building = new Building();
        building.setName("Eski İsim");
        building.setAddress("Eski Adres");
        building.setTotalFloors(2);
        building = buildingRepository.save(building);
        Long id = building.getId();

        // 2. UPDATE TESTİ
        UpdateBuildingRequest updateRequest = new UpdateBuildingRequest();
        updateRequest.setName("Yeni İsim"); // Setter çalıştı
        updateRequest.setAddress("Yeni Adres");
        updateRequest.setTotalFloors(5);

        mockMvc.perform(put("/api/buildings/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yeni İsim")); // Getter çalıştı

        // DB kontrolü (Emin olmak için)
        Building updated = buildingRepository.findById(id).orElseThrow();
        assertEquals("Yeni İsim", updated.getName());

        // 3. DELETE TESTİ
        mockMvc.perform(delete("/api/buildings/" + id))
                .andExpect(status().isNoContent()); // 204 bekliyoruz

        // DB kontrolü: Artık olmamalı
        assertFalse(buildingRepository.existsById(id));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBuilding_WhenIdNotExists_ShouldReturn400() throws Exception {
        // Olmayan bir ID'yi silmeye çalışırsak Exception fırlatmalı
        mockMvc.perform(delete("/api/buildings/99999"))
                .andExpect(status().isBadRequest()); // BusinessException
    }
}