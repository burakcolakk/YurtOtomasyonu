package com.yurt.project.repository;

import com.yurt.project.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // Bir binaya ait odaları getir
    List<Room> findByBuilding_Id(Long buildingId);

    // Oda numarasına göre bul (Örn: 101)
    Optional<Room> findByRoomNumber(String roomNumber);
}