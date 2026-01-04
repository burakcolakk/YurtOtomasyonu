package com.yurt.project.repository;
import com.yurt.project.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StaffRepository extends JpaRepository<Staff, Long> {}