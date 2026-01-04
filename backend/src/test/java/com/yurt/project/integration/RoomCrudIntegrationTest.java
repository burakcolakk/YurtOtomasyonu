package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateRoomRequest;
import com.yurt.project.dto.request.UpdateRoomRequest;
import com.yurt.project.entity.Building;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoomRepository;
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
class RoomCrudIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BuildingRepository buildingRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void roomLifecycle_ShouldWork() throws Exception {
        // 1. HAZIRLIK: Önce bir Bina lazım
        Building building = new Building();
        building.setName("CRUD Test Blok");
        building.setAddress("Kampüs");
        building.setTotalFloors(3);
        building = buildingRepository.save(building);

        // 2. OLUŞTURMA (Create)
        CreateRoomRequest createRequest = new CreateRoomRequest();
        createRequest.setRoomNumber("101-Test");
        createRequest.setBuildingId(building.getId());
        createRequest.setCapacity(3);
        createRequest.setFloorNumber(1);

        String responseJson = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Oluşan odayı DB'den bul
        Long roomId = roomRepository.findByRoomNumber("101-Test").orElseThrow().getId();

        // 3. GÜNCELLEME (Update)
        UpdateRoomRequest updateRequest = new UpdateRoomRequest();
        updateRequest.setRoomNumber("101-Updated"); // Numara değişti
        updateRequest.setCapacity(5);               // Kapasite arttı

        mockMvc.perform(put("/api/rooms/" + roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(5));

        // 4. SİLME (Delete)
        mockMvc.perform(delete("/api/rooms/" + roomId))
                .andExpect(status().isNoContent()); // 204

        // 5. KONTROL (Gerçekten silindi mi?)
        assertFalse(roomRepository.existsById(roomId));
    }
}