package com.yurt.project.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumTest {

    @Test
    void testTicketStatus() {
        // 1. Tüm değerleri çek (values metodu coverage için kritiktir)
        TicketStatus[] statuses = TicketStatus.values();

        // 2. Sayı kontrolü (OPEN, IN_PROGRESS, RESOLVED, CLOSED -> 4 tane)
        assertEquals(4, statuses.length);

        // 3. String'den Enum'a çevirme (valueOf metodu coverage için kritiktir)
        assertEquals(TicketStatus.OPEN, TicketStatus.valueOf("OPEN"));
        assertEquals(TicketStatus.IN_PROGRESS, TicketStatus.valueOf("IN_PROGRESS"));
        assertEquals(TicketStatus.RESOLVED, TicketStatus.valueOf("RESOLVED"));
        assertEquals(TicketStatus.CLOSED, TicketStatus.valueOf("CLOSED"));
    }

    @Test
    void testLeaveStatus() {
        // 1. Tüm değerleri çek
        LeaveStatus[] statuses = LeaveStatus.values();

        // 2. Sayı kontrolü (PENDING, APPROVED, REJECTED -> 3 tane)
        assertEquals(3, statuses.length);

        // 3. String dönüşüm kontrolü
        assertEquals(LeaveStatus.PENDING, LeaveStatus.valueOf("PENDING"));
        assertEquals(LeaveStatus.APPROVED, LeaveStatus.valueOf("APPROVED"));
        assertEquals(LeaveStatus.REJECTED, LeaveStatus.valueOf("REJECTED"));
    }
}