package com.yurt.project.service.impl;

import com.yurt.project.entity.LeaveRequest;
import com.yurt.project.entity.Student;
import com.yurt.project.enums.LeaveStatus;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.LeaveRequestRepository;
import com.yurt.project.repository.StudentRepository;
import com.yurt.project.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final StudentRepository studentRepository;

    @Override
    public LeaveRequest createLeaveRequest(Long studentId, LocalDate start, LocalDate end, String reason) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("Öğrenci bulunamadı"));

        if (start.isAfter(end)) {
            throw new BusinessException("Başlangıç tarihi bitiş tarihinden sonra olamaz.");
        }

        LeaveRequest request = new LeaveRequest();
        request.setStudent(student);
        request.setStartDate(start);
        request.setEndDate(end);
        request.setReason(reason);
        request.setStatus(LeaveStatus.PENDING); // İlk başta beklemede

        return leaveRequestRepository.save(request);
    }

    @Override
    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("İzin talebi bulunamadı"));

        request.setStatus(status);
        return leaveRequestRepository.save(request);
    }

    @Override
    public List<LeaveRequest> getAllRequests() {
        return leaveRequestRepository.findAll();
    }

    @Override
    public List<LeaveRequest> getStudentRequests(Long studentId) {
        // Repository'e findByStudent_Id eklendiğini varsayıyoruz veya filtreliyoruz:
        return leaveRequestRepository.findAll().stream()
                .filter(l -> l.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }
}