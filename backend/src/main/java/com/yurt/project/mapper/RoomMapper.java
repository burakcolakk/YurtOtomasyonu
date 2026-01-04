package com.yurt.project.mapper;

import com.yurt.project.dto.request.CreateRoomRequest;
import com.yurt.project.dto.response.RoomResponse;
import com.yurt.project.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    // Request'ten Entity'ye çevirirken buildingId gibi alanları Service'de elle set edebiliriz
    // veya MapStruct'a ignore diyebiliriz. Şimdilik düz çeviri yapıyoruz.
    @Mapping(target = "building", ignore = true) // Binayı service katmanında bulup set edeceğiz
    Room toEntity(CreateRoomRequest request);

    // Entity'den Response'a çevirirken Binanın adını da ekleyelim
    @Mapping(source = "building.name", target = "buildingName")
    RoomResponse toResponse(Room room);
}