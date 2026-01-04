package com.yurt.project.repository;

import com.yurt.project.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    // Bir öğrencinin aktif kaydı var mı?
    boolean existsByStudent_IdAndIsActiveTrue(Long studentId);

    // Aktif olan tüm yerleşimleri getir
    List<Allocation> findByIsActiveTrue();
}