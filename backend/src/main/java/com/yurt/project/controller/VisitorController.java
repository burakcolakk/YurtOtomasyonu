package com.yurt.project.controller;

import com.yurt.project.dto.request.CreateVisitorRequest;
import com.yurt.project.dto.response.VisitorResponse;
import com.yurt.project.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    // Testin patladığı yer burasıydı (POST metodu yoktu)
    @PostMapping
    public ResponseEntity<VisitorResponse> createVisitor(@Valid @RequestBody CreateVisitorRequest request) {
        return new ResponseEntity<>(visitorService.createVisitor(request), HttpStatus.CREATED);
    }

    // Testin ikinci aşaması için gerekli (Çıkış yapma)
    @PutMapping("/{id}/checkout")
    public ResponseEntity<Void> checkOutVisitor(@PathVariable Long id) {
        visitorService.checkOutVisitor(id);
        return ResponseEntity.ok().build();
    }
}