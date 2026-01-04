package com.yurt.project.mapper;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.response.StudentResponse;
import com.yurt.project.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // Request -> Entity
    Student toEntity(CreateStudentRequest request);

    // Entity -> Response
    // (MapStruct isimler aynıysa otomatik eşleşir ama bazen manuel tetiklemek gerekir)
    @Mapping(target = "roomNumber", source = "room.roomNumber") // Oda numarasını da getirsin
    StudentResponse toResponse(Student student);
}