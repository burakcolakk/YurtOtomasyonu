package com.yurt.project.repository;
import com.yurt.project.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {}