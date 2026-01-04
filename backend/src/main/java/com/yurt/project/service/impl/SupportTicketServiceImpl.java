package com.yurt.project.service.impl;

import com.yurt.project.entity.SupportTicket;
import com.yurt.project.entity.User;
import com.yurt.project.enums.TicketStatus;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.SupportTicketRepository;
import com.yurt.project.repository.UserRepository;
import com.yurt.project.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public SupportTicket createTicket(Long userId, String title, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));

        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(TicketStatus.OPEN);

        return ticketRepository.save(ticket);
    }

    @Override
    public SupportTicket updateTicketStatus(Long ticketId, TicketStatus status) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Talep bulunamadı"));

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return ticketRepository.findAll();
    }
}