package com.yurt.project.dto.response;

import lombok.Data;

@Data
public class DashboardStats {
    private long totalStudents;
    private long totalRooms;
    private long totalBeds;
    private long occupiedBeds; // Dolu yataklar
    private long emptyBeds;    // Boş yataklar
    private long pendingLeaves;// Bekleyen izinler
    private long openTickets;  // Açık arızalar
}