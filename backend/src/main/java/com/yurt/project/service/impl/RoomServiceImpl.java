package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateRoomRequest;
import com.yurt.project.dto.request.UpdateRoomRequest;
import com.yurt.project.dto.response.RoomResponse;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.RoomMapper;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final RoomMapper roomMapper;


    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        // 1. Bina var mı?
        Building building = buildingRepository.findById(request.getBuildingId())
                .orElseThrow(() -> new BusinessException("Bina bulunamadı."));

        // 2. Oda daha önce oluşturulmuş mu?
        if (roomRepository.findByRoomNumber(request.getRoomNumber()).isPresent()) {
            throw new BusinessException("Bu numaraya sahip bir oda zaten var: " + request.getRoomNumber());
        }

        // 3. Oda oluşturma
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setFloorNumber(request.getFloorNumber());
        room.setBuilding(building);
        room.setCurrentOccupancy(0); // Yeni oda boştur

        roomRepository.save(room);

        // 4. Response Dönüşü (Basit manuel mapping)
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setBuildingName(building.getName());
        return response;
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        // Basit listeleme
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> responses = new ArrayList<>();

        for (Room room : rooms) {
            RoomResponse resp = new RoomResponse();
            resp.setId(room.getId());
            resp.setRoomNumber(room.getRoomNumber());
            resp.setCapacity(room.getCapacity());
            resp.setCurrentOccupancy(room.getCurrentOccupancy());
            resp.setBuildingName(room.getBuilding().getName());
            responses.add(resp);
        }
        return responses;
    }
    @Override
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Oda bulunamadı id: " + id));

        // Doluluk kontrolü: Kapasiteyi mevcut kişi sayısından aza düşüremezsin!
        if (request.getCapacity() != null && request.getCapacity() < room.getCurrentOccupancy()) {
            throw new BusinessException("Kapasite, mevcut öğrenci sayısından az olamaz!");
        }

        if (request.getRoomNumber() != null) room.setRoomNumber(request.getRoomNumber());
        if (request.getCapacity() != null) room.setCapacity(request.getCapacity());
        if (request.getFloorNumber() != null) room.setFloorNumber(request.getFloorNumber());

        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Silinecek oda bulunamadı id: " + id));

        // Güvenlik: İçinde öğrenci varken oda silinemez
        if (room.getCurrentOccupancy() > 0) {
            throw new BusinessException("İçinde öğrenci olan oda silinemez!");
        }

        roomRepository.deleteById(id);
    }
}