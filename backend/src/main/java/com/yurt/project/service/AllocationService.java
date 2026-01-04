package com.yurt.project.service;

import com.yurt.project.dto.request.AllocationRequest;

public interface AllocationService {
    void allocateStudentToBed(AllocationRequest request);
}