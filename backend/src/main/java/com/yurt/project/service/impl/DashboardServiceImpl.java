package com.yurt.project.service.impl;

import com.yurt.project.dto.response.DashboardStats;
import com.yurt.project.enums.LeaveStatus;
import com.yurt.project.enums.TicketStatus;
import com.yurt.project.repository.*;
import com.yurt.project.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final SupportTicketRepository ticketRepository;

    @Override
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();

        stats.setTotalStudents(studentRepository.count());
        stats.setTotalRooms(roomRepository.count());
        stats.setTotalBeds(bedRepository.count());

        // Bu sorguları repository'e eklemediysek Java tarafında filtreleyebiliriz (Veri azken sorun olmaz)
        // Ama doğrusu Repository'de countByIsOccupiedTrue() gibi metodlar olmasıdır.
        // Şimdilik mantığı kuralım:

        long occupied = bedRepository.findAll().stream().filter(b -> b.isOccupied()).count();
        stats.setOccupiedBeds(occupied);
        stats.setEmptyBeds(stats.getTotalBeds() - occupied);

        long pendingLeaves = leaveRequestRepository.findAll().stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING).count();
        stats.setPendingLeaves(pendingLeaves);

        long openTickets = ticketRepository.findAll().stream()
                .filter(t -> t.getStatus() == TicketStatus.OPEN).count();
        stats.setOpenTickets(openTickets);

        return stats;
    }
}