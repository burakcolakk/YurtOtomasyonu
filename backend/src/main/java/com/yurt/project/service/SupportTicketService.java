package com.yurt.project.service;

import com.yurt.project.entity.SupportTicket;
import com.yurt.project.enums.TicketStatus;
import java.util.List;

public interface SupportTicketService {
    SupportTicket createTicket(Long userId, String title, String description);
    SupportTicket updateTicketStatus(Long ticketId, TicketStatus status);
    List<SupportTicket> getAllTickets();
}