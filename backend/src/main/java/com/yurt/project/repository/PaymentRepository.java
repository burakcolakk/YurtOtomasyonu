package com.yurt.project.repository;
import com.yurt.project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudent_Id(Long studentId); // Öğrencinin ödemeleri
}