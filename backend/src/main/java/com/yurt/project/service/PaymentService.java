package com.yurt.project.service;
import com.yurt.project.entity.Payment;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    void createPayment(Long studentId, BigDecimal amount, String description);
    List<Payment> getStudentPayments(Long studentId);
}