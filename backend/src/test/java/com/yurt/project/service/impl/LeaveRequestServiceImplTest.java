package com.yurt.project.service.impl;

import com.yurt.project.entity.Student;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.LeaveRequestRepository;
import com.yurt.project.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceImplTest {

    @Mock private LeaveRequestRepository leaveRequestRepository;
    @Mock private StudentRepository studentRepository;
    @InjectMocks private LeaveRequestServiceImpl leaveRequestService;

    @Test
    void createLeaveRequest_WhenStartAfterEnd_ShouldThrowException() {
        // 1. HAZIRLIK
        LocalDate start = LocalDate.now().plusDays(5); // 5 gün sonra
        LocalDate end = LocalDate.now().plusDays(1);   // 1 gün sonra (HATA!)

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));

        // 2. KONTROL
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            leaveRequestService.createLeaveRequest(1L, start, end, "Tatil");
        });

        assertEquals("Başlangıç tarihi bitiş tarihinden sonra olamaz.", ex.getMessage());
        verify(leaveRequestRepository, never()).save(any());
    }
}