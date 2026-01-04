package com.yurt.project.mapper;

import com.yurt.project.dto.request.CreateBuildingRequest;
import com.yurt.project.dto.response.BuildingResponse;
import com.yurt.project.entity.Building;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    // Request -> Entity
    Building toEntity(CreateBuildingRequest request);

    // Entity -> Response (Bunu eklediğinden emin ol)
    BuildingResponse toResponse(Building building);
}