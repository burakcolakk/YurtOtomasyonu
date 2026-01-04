package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateRoomRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BuildingAndRoomIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BuildingRepository buildingRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAndListBuildings_ShouldWork() throws Exception {
        // 1. Bina Oluşturma (POST)
        mockMvc.perform(post("/api/buildings")
                        .param("name", "Mühendislik Bloğu")
                        .param("address", "Kampüs")
                        .param("totalFloors", "5"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mühendislik Bloğu"));

        // 2. Binaları Listeleme (GET)
        mockMvc.perform(get("/api/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRoom_ShouldWork() throws Exception {
        // Önce DB'ye bir bina atalım
        Building building = new Building();
        building.setName("Test Blok");
        building.setAddress("Test Adres");
        building.setTotalFloors(3);
        building = buildingRepository.save(building);

        // Odayı bu binaya ekleyelim
        CreateRoomRequest request = new CreateRoomRequest();
        request.setRoomNumber("Z-01");
        request.setBuildingId(building.getId());
        request.setCapacity(3);
        request.setFloorNumber(0);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomNumber").value("Z-01"));

        // Odaları Listele
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}