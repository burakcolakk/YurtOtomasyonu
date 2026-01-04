package com.yurt.project.controller;

import com.yurt.project.dto.request.AllocationRequest;
import com.yurt.project.service.AllocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/allocations")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<String> allocateStudent(@Valid @RequestBody AllocationRequest request) {
        allocationService.allocateStudentToBed(request);
        return new ResponseEntity<>("Öğrenci başarıyla odaya yerleştirildi.", HttpStatus.CREATED);
    }
}