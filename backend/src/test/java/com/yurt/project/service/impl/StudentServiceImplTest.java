package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.response.StudentResponse;
import com.yurt.project.entity.Building;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.Student;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.StudentMapper;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock // <-- EKSİK OLAN PARÇA BUYDU!
    private RoomRepository roomRepository;

    @Mock
    private StudentMapper studentMapper;

    @Test
    void createStudent_WithValidRequest_ShouldReturnResponse() {
        // 1. HAZIRLIK
        CreateStudentRequest request = new CreateStudentRequest();
        request.setTcKimlikNo("12345678901");
        request.setFirstName("Ahmet");
        request.setRoomId(1L);

        // Mock Oda (Kapasitesi uygun)
        Room room = new Room();
        room.setId(1L);
        room.setCapacity(3);
        room.setCurrentOccupancy(1); // Yer var

        // Mock Öğrenci
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Ahmet");

        StudentResponse response = new StudentResponse();
        response.setId(1L);
        response.setFirstName("Ahmet");

        // 2. MOCK DAVRANIŞLARI
        // Odayı bulduğunda bunu dön
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        // Öğrenciyi çevir
        when(studentMapper.toEntity(request)).thenReturn(student);
        // Kaydet
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        // Cevabı çevir
        when(studentMapper.toResponse(student)).thenReturn(response);

        // 3. EYLEM
        StudentResponse result = studentService.createStudent(request);

        // 4. KONTROL
        assertNotNull(result);
        assertEquals("Ahmet", result.getFirstName());
        verify(roomRepository).save(room); // Odanın güncellendiğini doğrula (Kişi sayısı artmalı)
    }

    @Test
    void createStudent_WhenRoomNotFound_ShouldThrowException() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setRoomId(99L); // Olmayan oda

        // Oda bulunamazsa boş dön
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> studentService.createStudent(request));
    }

    @Test
    void createStudent_WhenRoomIsFull_ShouldThrowException() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setRoomId(1L);

        // Dolu Oda
        Room fullRoom = new Room();
        fullRoom.setCapacity(2);
        fullRoom.setCurrentOccupancy(2); // Kapasite dolu

        when(roomRepository.findById(1L)).thenReturn(Optional.of(fullRoom));

        // Hata fırlatmalı
        BusinessException exception = assertThrows(BusinessException.class,
                () -> studentService.createStudent(request));

        assertTrue(exception.getMessage().contains("dolu"));
    }
}