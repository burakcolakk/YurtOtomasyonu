package com.yurt.project.service.impl;

import com.yurt.project.entity.Payment;
import com.yurt.project.entity.Student;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.PaymentRepository;
import com.yurt.project.repository.StudentRepository;
import com.yurt.project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    @Override
    public void createPayment(Long studentId, BigDecimal amount, String description) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("Öğrenci bulunamadı"));

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setAmount(amount);
        payment.setDescription(description);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaid(true); // Direkt ödendi varsayıyoruz

        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getStudentPayments(Long studentId) {
        return paymentRepository.findByStudent_Id(studentId);
    }
}