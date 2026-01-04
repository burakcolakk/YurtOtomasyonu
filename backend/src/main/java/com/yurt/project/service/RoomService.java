package com.yurt.project.service;

import com.yurt.project.dto.request.CreateRoomRequest;
import com.yurt.project.dto.request.UpdateRoomRequest;
import com.yurt.project.dto.response.RoomResponse;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(CreateRoomRequest request);
    List<RoomResponse> getAllRooms();
    RoomResponse updateRoom(Long id, UpdateRoomRequest request);
    void deleteRoom(Long id);
}