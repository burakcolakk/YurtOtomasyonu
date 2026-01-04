package com.yurt.project.service;

import com.yurt.project.dto.request.CreateVisitorRequest;
import com.yurt.project.dto.response.VisitorResponse;
import com.yurt.project.entity.Visitor;
import java.util.List;

public interface VisitorService {
    VisitorResponse createVisitor(CreateVisitorRequest request);
    List<Visitor> getAllVisitors();
    void checkOutVisitor(Long id); // Çıkış yap metodu
}