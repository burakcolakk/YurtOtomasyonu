package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateVisitorRequest;
import com.yurt.project.dto.response.VisitorResponse;
import com.yurt.project.entity.Visitor;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.VisitorMapper;
import com.yurt.project.repository.VisitorRepository;
import com.yurt.project.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;
    // private final StudentRepository studentRepository; // <-- SİLİNDİ (Kullanılmıyordu)

    @Override
    public VisitorResponse createVisitor(CreateVisitorRequest request) { // Parametre ismi 'request'
        Visitor visitor = visitorMapper.toEntity(request);

        // NullPointerException riskini önlemek için 'entryTime' burada set ediliyor
        visitor.setEntryTime(LocalDateTime.now());

        Visitor savedVisitor = visitorRepository.save(visitor);
        return visitorMapper.toResponse(savedVisitor);
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    @Override
    public void checkOutVisitor(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                // Lambda statement -> Expression Lambda dönüşümü yapıldı (Daha temiz kod)
                .orElseThrow(() -> new BusinessException("Ziyaretçi bulunamadı id: " + id));

        // Çıkış saati set etme
        visitor.setExitTime(LocalDateTime.now());
        visitorRepository.save(visitor);
    }
}