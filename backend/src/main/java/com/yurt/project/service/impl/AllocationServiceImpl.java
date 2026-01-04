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
import com.yurt.project.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final StudentRepository studentRepository;
    private final BedRepository bedRepository;

    @Override
    @Transactional // Ya hep ya hiç! Hata olursa işlemi geri al.
    public void allocateStudentToBed(AllocationRequest request) {

        // 1. Öğrenci kontrolü
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new BusinessException("Öğrenci bulunamadı"));

        // 2. Öğrenci zaten bir yerde kalıyor mu?
        boolean isAlreadyAllocated = allocationRepository.existsByStudent_IdAndIsActiveTrue(student.getId());
        if (isAlreadyAllocated) {
            throw new BusinessException("Bu öğrenci zaten bir odaya yerleştirilmiş!");
        }

        // 3. Yatak kontrolü
        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new BusinessException("Yatak bulunamadı"));

        // 4. Yatak dolu mu?
        if (bed.isOccupied()) {
            throw new BusinessException("Seçilen yatak maalesef dolu.");
        }

        // 5. Oda kapasite kontrolü (Yatak boş olsa bile oda dolu olabilir mi? Mantıken hayır ama kontrol edelim)
        Room room = bed.getRoom();
        if (room.getCurrentOccupancy() >= room.getCapacity()) {
            throw new BusinessException("Bu oda tamamen dolu.");
        }

        // --- İŞLEMLER BAŞLIYOR ---

        // A. Yerleşimi kaydet
        Allocation allocation = new Allocation();
        allocation.setStudent(student);
        allocation.setBed(bed);
        allocation.setStartDate(request.getStartDate());
        allocation.setEndDate(request.getEndDate());
        allocation.setActive(true);
        allocationRepository.save(allocation);

        // B. Yatağı dolu olarak işaretle
        bed.setOccupied(true);
        bedRepository.save(bed);

        // C. Odanın mevcut kişi sayısını artır
        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
        // RoomRepository'e gerek yok, Bed üzerinden odaya eriştik ve Hibernate bunu yönetir.
        // Ancak garanti olsun diye roomRepository.save(room) diyebilirsin veya Transactional commit eder.
    }
}