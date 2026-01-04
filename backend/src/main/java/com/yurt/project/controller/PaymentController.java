package com.yurt.project.controller;

import com.yurt.project.entity.Payment;
import com.yurt.project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestParam Long studentId,
                                                @RequestParam BigDecimal amount,
                                                @RequestParam String description) {
        paymentService.createPayment(studentId, amount, description);
        return new ResponseEntity<>("Ödeme başarıyla alındı.", HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Payment>> getStudentPayments(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getStudentPayments(studentId));
    }
}