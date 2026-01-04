package com.yurt.project.service;

import com.yurt.project.entity.Bed;
import java.util.List;

public interface BedService {
    Bed addBedToRoom(Long roomId, String bedName);
    void removeBed(Long bedId);
    List<Bed> getBedsByRoomId(Long roomId);
}