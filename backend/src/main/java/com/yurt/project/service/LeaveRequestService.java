package com.yurt.project.service;

import com.yurt.project.entity.LeaveRequest;
import com.yurt.project.enums.LeaveStatus;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {
    LeaveRequest createLeaveRequest(Long studentId, LocalDate start, LocalDate end, String reason);
    LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status); // Onayla/Reddet
    List<LeaveRequest> getAllRequests();
    List<LeaveRequest> getStudentRequests(Long studentId);
}