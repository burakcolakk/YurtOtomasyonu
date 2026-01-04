package com.yurt.project.controller;

import com.yurt.project.entity.SupportTicket;
import com.yurt.project.enums.TicketStatus;
import com.yurt.project.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService ticketService;

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestParam Long userId,
                                                      @RequestParam String title,
                                                      @RequestParam String description) {
        return new ResponseEntity<>(
                ticketService.createTicket(userId, title, description),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SupportTicket> updateTicketStatus(@PathVariable Long id,
                                                            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}