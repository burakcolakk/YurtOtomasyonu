package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateStaffRequest;
import com.yurt.project.dto.request.UpdateStaffRequest;
import com.yurt.project.dto.response.StaffResponse;
import com.yurt.project.entity.Staff;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.StaffMapper;
import com.yurt.project.repository.StaffRepository;
import com.yurt.project.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    @Override
    public StaffResponse createStaff(CreateStaffRequest request) {
        Staff staff = staffMapper.toEntity(request);
        Staff savedStaff = staffRepository.save(staff);
        return staffMapper.toResponse(savedStaff);
    }

    // HATA VEREN KISIM BURASIYDI: Dönüş tipi List<StaffResponse> olarak düzeltildi
    @Override
    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(staffMapper::toResponse) // Entity -> DTO dönüşümü
                .collect(Collectors.toList());
    }

    // EKSİK OLAN METOD EKLENDİ
    @Override
    public StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Personel bulunamadı id: " + id));
        return staffMapper.toResponse(staff);
    }

    @Override
    public StaffResponse updateStaff(Long id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Personel bulunamadı id: " + id));

        if (request.getFirstName() != null) staff.setFirstName(request.getFirstName());
        if (request.getLastName() != null) staff.setLastName(request.getLastName());
        if (request.getJobTitle() != null) staff.setJobTitle(request.getJobTitle());
        if (request.getPhoneNumber() != null) staff.setPhoneNumber(request.getPhoneNumber());

        Staff updatedStaff = staffRepository.save(staff);
        return staffMapper.toResponse(updatedStaff);
    }

    @Override
    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new BusinessException("Silinecek personel bulunamadı id: " + id);
        }
        staffRepository.deleteById(id);
    }
}