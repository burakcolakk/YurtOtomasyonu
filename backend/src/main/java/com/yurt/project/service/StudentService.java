package com.yurt.project.service;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.request.UpdateStudentRequest;
import com.yurt.project.dto.response.StudentResponse;
import java.util.List;

public interface StudentService {
    StudentResponse createStudent(CreateStudentRequest request);
    List<StudentResponse> getAllStudents();
    StudentResponse getStudentById(Long id);
    StudentResponse updateStudent(Long id, UpdateStudentRequest request);
    void deleteStudent(Long id);
}