package com.yurt.project.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Integer capacity;
    private Integer currentOccupancy; // Şu an kaç kişi var?
    private String buildingName;      // Hangi binada?

    private List<BedResponse> beds;   // İçindeki yataklar

}