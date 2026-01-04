package com.yurt.project.service.impl;

import com.yurt.project.dto.request.AllocationRequest;
import com.yurt.project.entity.Allocation;
import com.yurt.project.entity.Bed;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.Student;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.AllocationRepository;
import com.yurt.project.repository.BedRepository;
import com.yurt.project.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllocationServiceImplTest {

    @Mock private AllocationRepository allocationRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private BedRepository bedRepository;

    @InjectMocks
    private AllocationServiceImpl allocationService;

    @Test
    void allocateStudent_WhenBedIsOccupied_ShouldThrowException() {
        // 1. HAZIRLIK
        AllocationRequest request = new AllocationRequest();
        request.setStudentId(1L);
        request.setBedId(10L);

        Student student = new Student();
        student.setId(1L);

        // Dolu bir yatak simüle ediyoruz
        Bed bed = new Bed();
        bed.setId(10L);
        bed.setOccupied(true); // DİKKAT: Yatak zaten dolu!

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(allocationRepository.existsByStudent_IdAndIsActiveTrue(1L)).thenReturn(false); // Öğrenci boşta
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));

        // 2. ve 3. KONTROL
        // Dolu yatağa atama yapınca hata bekliyoruz
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            allocationService.allocateStudentToBed(request);
        });

        assertEquals("Seçilen yatak maalesef dolu.", exception.getMessage());

        // Asla kayıt yapılmamalı
        verify(allocationRepository, never()).save(any(Allocation.class));
    }

    @Test
    void allocateStudent_WhenSuccessful_ShouldSaveAllocationAndUpdateBed() {
        // 1. HAZIRLIK (Mutlu Yol)
        AllocationRequest request = new AllocationRequest();
        request.setStudentId(1L);
        request.setBedId(20L);

        Student student = new Student();
        student.setId(1L);

        Room room = new Room();
        room.setCapacity(4);
        room.setCurrentOccupancy(1);

        Bed bed = new Bed();
        bed.setId(20L);
        bed.setOccupied(false); // Yatak boş
        bed.setRoom(room);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(allocationRepository.existsByStudent_IdAndIsActiveTrue(1L)).thenReturn(false);
        when(bedRepository.findById(20L)).thenReturn(Optional.of(bed));

        // 2. EYLEM
        allocationService.allocateStudentToBed(request);

        // 3. KONTROL
        assertTrue(bed.isOccupied()); // Yatak hafızada dolu olarak işaretlendi mi?
        assertEquals(2, room.getCurrentOccupancy()); // Oda sayısı arttı mı?

        verify(allocationRepository, times(1)).save(any(Allocation.class)); // Kayıt atıldı mı?
        verify(bedRepository, times(1)).save(bed); // Yatak güncellendi mi?
    }
}