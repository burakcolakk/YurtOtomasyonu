package com.yurt.project.repository;
import com.yurt.project.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {}