package com.yurt.project.service.impl;

import com.yurt.project.entity.Payment;
import com.yurt.project.entity.Student;
import com.yurt.project.repository.PaymentRepository;
import com.yurt.project.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private StudentRepository studentRepository;
    @InjectMocks private PaymentServiceImpl paymentService;

    @Test
    void createPayment_ShouldSavePaymentWithCorrectDetails() {
        // 1. HAZIRLIK
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Veli");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // 2. EYLEM
        paymentService.createPayment(1L, new BigDecimal("1500.00"), "Yurt Taksidi");

        // 3. KONTROL (Kaydedilen nesneyi yakala ve incele)
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();

        assertEquals(new BigDecimal("1500.00"), savedPayment.getAmount());
        assertEquals("Yurt Taksidi", savedPayment.getDescription());
        assertNotNull(savedPayment.getPaymentDate()); // Tarih boş olmamalı
        assertTrue(savedPayment.isPaid()); // Ödendi olarak işaretlenmeli
    }
}