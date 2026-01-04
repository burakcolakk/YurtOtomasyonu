package com.yurt.project.controller;
import com.yurt.project.entity.LeaveRequest;
import com.yurt.project.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveService;

    @PostMapping
    public LeaveRequest create(@RequestParam Long studentId, @RequestParam LocalDate start,
                               @RequestParam LocalDate end, @RequestParam String reason) {
        return leaveService.createLeaveRequest(studentId, start, end, reason);
    }

    @GetMapping
    public List<LeaveRequest> getAll() { return leaveService.getAllRequests(); }
}