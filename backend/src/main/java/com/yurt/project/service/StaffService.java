package com.yurt.project.service;

import com.yurt.project.dto.request.CreateStaffRequest;
import com.yurt.project.dto.request.UpdateStaffRequest;
import com.yurt.project.dto.response.StaffResponse;
import com.yurt.project.entity.Staff; // Bu import entity dönen metod kalırsa diye dursun ama genelde DTO döneceğiz

import java.util.List;

public interface StaffService {

    // Artık String parametreler yerine DTO alıyor
    StaffResponse createStaff(CreateStaffRequest request);

    // DİKKAT: Dönüş tipi List<Staff> değil, List<StaffResponse> olmalı!
    List<StaffResponse> getAllStaff();

    // Eksik olan metod buydu:
    StaffResponse getStaffById(Long id);

    // Update işlemi
    StaffResponse updateStaff(Long id, UpdateStaffRequest request);

    // Silme işlemi
    void deleteStaff(Long id);
}