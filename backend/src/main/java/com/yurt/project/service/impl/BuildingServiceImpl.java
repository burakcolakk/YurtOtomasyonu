package com.yurt.project.service.impl;

import com.yurt.project.dto.request.UpdateBuildingRequest;
import com.yurt.project.dto.response.BuildingResponse;
import com.yurt.project.entity.Building;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.BuildingMapper;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingMapper buildingMapper;

    @Override
    public Building createBuilding(String name, String address, Integer totalFloors) {
        Building building = new Building();
        building.setName(name);
        building.setAddress(address);
        building.setTotalFloors(totalFloors);
        return buildingRepository.save(building);
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Bina bulunamadı: " + id));
    }

    // GÜNCELLEME (Update)
    @Override
    public BuildingResponse updateBuilding(Long id, UpdateBuildingRequest request) {
        // 1. Binayı bul
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Güncellenecek bina bulunamadı."));

        // 2. Alanları güncelle (Eğer null değilse güncelle kontrolü de yapılabilir ama şimdilik direkt atıyoruz)
        if (request.getName() != null) building.setName(request.getName());
        if (request.getAddress() != null) building.setAddress(request.getAddress());
        if (request.getTotalFloors() != null) building.setTotalFloors(request.getTotalFloors());

        // 3. Kaydet
        Building updatedBuilding = buildingRepository.save(building);

        // 4. Cevaba dönüştür
        return buildingMapper.toResponse(updatedBuilding);
    }

@Override
public void deleteBuilding(Long id) {
    // 1. Önce veritabanında var mı kontrol et
    if (!buildingRepository.existsById(id)) {
        throw new BusinessException("Silinecek bina bulunamadı id: " + id);
    }

    // 2. Varsa sil
    buildingRepository.deleteById(id);
}
}
