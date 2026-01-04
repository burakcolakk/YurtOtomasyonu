package com.yurt.project.controller;

import com.yurt.project.entity.Bed;
import com.yurt.project.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    @PostMapping
    public ResponseEntity<Bed> addBedToRoom(@RequestParam Long roomId, @RequestParam String bedName) {
        return new ResponseEntity<>(bedService.addBedToRoom(roomId, bedName), HttpStatus.CREATED);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Bed>> getBedsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(bedService.getBedsByRoomId(roomId));
    }
}