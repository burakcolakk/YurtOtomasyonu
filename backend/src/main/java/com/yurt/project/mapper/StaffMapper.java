package com.yurt.project.mapper;

import com.yurt.project.dto.request.CreateStaffRequest; // Bu dosyanın olduğundan emin ol
import com.yurt.project.dto.response.StaffResponse;
import com.yurt.project.entity.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    // Request -> Entity
    Staff toEntity(CreateStaffRequest request);

    // Entity -> Response
    StaffResponse toResponse(Staff staff);
}