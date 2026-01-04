package com.yurt.project.controller;

import com.yurt.project.dto.request.UpdateBuildingRequest;
import com.yurt.project.dto.response.BuildingResponse;
import com.yurt.project.entity.Building;
import com.yurt.project.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestParam String name,
                                                   @RequestParam String address,
                                                   @RequestParam Integer totalFloors) {
        return new ResponseEntity<>(buildingService.createBuilding(name, address, totalFloors), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingResponse> updateBuilding(
            @PathVariable Long id,
            @RequestBody UpdateBuildingRequest request) {

        return ResponseEntity.ok(buildingService.updateBuilding(id, request));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);

        // 204 No Content: "İşlem başarılı ama sana gösterecek bir verim kalmadı (çünkü sildim)"
        return ResponseEntity.noContent().build();
    }
}
