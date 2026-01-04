package com.yurt.project.service.impl;

import com.yurt.project.entity.Bed;
import com.yurt.project.entity.Room;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.BedRepository;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;

    @Override
    public Bed addBedToRoom(Long roomId, String bedName) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Oda bulunamadı"));

        Bed bed = new Bed();
        bed.setBedName(bedName);
        bed.setRoom(room);
        bed.setOccupied(false); // Yeni yatak boştur

        return bedRepository.save(bed);
    }

    @Override
    public void removeBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new BusinessException("Yatak bulunamadı"));

        if (bed.isOccupied()) {
            throw new BusinessException("Dolu yatak silinemez! Önce öğrenci çıkışı yapın.");
        }
        bedRepository.delete(bed);
    }

    @Override
    public List<Bed> getBedsByRoomId(Long roomId) {
        // Bu metod için Repository'e "findByRoom_Id" eklemen gerekebilir veya stream ile filtreleyebilirsin.
        // Performans için Repository'e eklemek daha iyidir ama şimdilik örnek:
        return bedRepository.findAll().stream()
                .filter(b -> b.getRoom().getId().equals(roomId))
                .toList();
    }
}