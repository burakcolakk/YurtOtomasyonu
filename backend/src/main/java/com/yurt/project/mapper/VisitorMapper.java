package com.yurt.project.mapper;

import com.yurt.project.dto.request.CreateVisitorRequest;
import com.yurt.project.dto.response.VisitorResponse;
import com.yurt.project.entity.Visitor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    Visitor toEntity(CreateVisitorRequest request);
    VisitorResponse toResponse(Visitor visitor);
}