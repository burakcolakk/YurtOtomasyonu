package com.yurt.project.service;

import com.yurt.project.dto.request.UpdateBuildingRequest;
import com.yurt.project.dto.response.BuildingResponse;
import com.yurt.project.entity.Building;
import java.util.List;

public interface BuildingService {
    Building createBuilding(String name, String address, Integer totalFloors);
    List<Building> getAllBuildings();
    Building getBuildingById(Long id);

    // GÜNCELLEME (Update)
    BuildingResponse updateBuilding(Long id, UpdateBuildingRequest request);
    void deleteBuilding(Long id);
}