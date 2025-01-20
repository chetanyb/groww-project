package com.groww.housekeeping.controller;

import com.groww.housekeeping.service.HousekeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/housekeeping")
public class HousekeepingController {

    @Autowired
    private HousekeepingService housekeepingService;

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateStocks(@RequestPart("file") MultipartFile file) {
        housekeepingService.processCSV(file);
        return ResponseEntity.ok("File processed successfully");
    }
}
