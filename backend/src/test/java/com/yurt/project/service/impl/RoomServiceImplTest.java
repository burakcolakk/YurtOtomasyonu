package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateRoomRequest;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock private RoomRepository roomRepository;
    @Mock private BuildingRepository buildingRepository;
    @InjectMocks private RoomServiceImpl roomService;

    @Test
    void createRoom_WhenRoomNumberExists_ShouldThrowException() {
        // 1. HAZIRLIK
        CreateRoomRequest request = new CreateRoomRequest();
        request.setRoomNumber("101");
        request.setBuildingId(1L);

        when(buildingRepository.findById(1L)).thenReturn(Optional.of(new Building()));

        // "Bu oda numarası var mı?" sorusuna "Evet, var" döndürüyoruz
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.of(new Room()));

        // 2. KONTROL
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            roomService.createRoom(request);
        });

        assertTrue(ex.getMessage().contains("Bu numaraya sahip bir oda zaten var"));
        verify(roomRepository, never()).save(any());
    }
}