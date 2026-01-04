package com.yurt.project.controller;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.request.UpdateStudentRequest; // Import
import com.yurt.project.dto.response.StudentResponse;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.Student;
import com.yurt.project.mapper.StudentMapper;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.repository.StudentRepository;
import com.yurt.project.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final StudentMapper studentMapper;



    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    // --- YENİ EKLENEN ENDPOINT ---
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }
    // -----------------------------

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/room/{roomId}")
    public List<StudentResponse> getStudentsByRoom(@PathVariable Long roomId) {
        // 1. Repository'den Entity listesini çek (Bu List<Student> döner)
        List<Student> students = studentRepository.findByRoomId(roomId);

        // 2. Stream ve Mapper kullanarak List<StudentResponse> tipine dönüştür
        return students.stream()
                .map(studentMapper::toResponse) // Her bir Student'ı StudentResponse'a çevirir
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student student = studentRepository.findById(id).orElseThrow();
        Room room = student.getRoom();

        if (room != null) {
            // Odanın doluluğunu 1 azalt
            room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
            roomRepository.save(room);
        }

        studentRepository.delete(student);
        return ResponseEntity.noContent().build();
    }



}