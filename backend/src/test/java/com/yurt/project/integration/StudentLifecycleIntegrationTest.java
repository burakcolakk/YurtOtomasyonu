package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.request.UpdateStudentRequest;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.Student;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.repository.StudentRepository;
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
class StudentLifecycleIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private StudentRepository studentRepository;
    @Autowired private BuildingRepository buildingRepository; // Eklendi
    @Autowired private RoomRepository roomRepository;         // Eklendi
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void fullStudentLifecycle_ShouldWork() throws Exception {

        // 1. ÖN HAZIRLIK: Bina ve Oda Oluştur (Yoksa roomId patlar)
        Building building = new Building();
        building.setName("Test Blok A");
        building.setAddress("Kampüs");
        building.setTotalFloors(3);
        building = buildingRepository.save(building);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setCapacity(3);
        room.setCurrentOccupancy(0);
        room.setFloorNumber(1);
        room.setBuilding(building);
        room = roomRepository.save(room);

        // 2. CREATE (Öğrenci Ekleme)
        CreateStudentRequest createRequest = new CreateStudentRequest();
        createRequest.setTcKimlikNo("55555555555");
        createRequest.setFirstName("Lifecycle");
        createRequest.setLastName("Test");
        createRequest.setPhoneNumber("5551112233");
        // --- EKLENEN ZORUNLU ALANLAR ---
        createRequest.setEmail("lifecycle@test.com");
        createRequest.setRoomId(room.getId());
        // -------------------------------

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        // Öğrenciyi DB'den bul
        Student student = studentRepository.findByTcKimlikNo("55555555555").orElseThrow();
        Long studentId = student.getId();

        // 3. UPDATE (Güncelleme)
        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        updateRequest.setPhoneNumber("5559998877"); // Telefonu değiştiriyoruz

        mockMvc.perform(put("/api/students/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("5559998877"));

        // 4. DELETE (Silme)
        mockMvc.perform(delete("/api/students/" + studentId))
                .andExpect(status().isNoContent()); // 204

        // 5. KONTROL (Silindi mi?)
        assertFalse(studentRepository.existsById(studentId));
    }
}