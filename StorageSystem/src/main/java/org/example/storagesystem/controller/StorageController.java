package org.example.storagesystem.controller;

import org.example.storagesystem.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/chunks")
    public ResponseEntity<String> storeChunk(
            @RequestParam("chunkId") String chunkId,
            @RequestParam("file") MultipartFile file) {
        try {
            storageService.storeChunk(chunkId, file);
            return ResponseEntity.ok("Chunk stored successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to store chunk: " + e.getMessage());
        }
    }

    @GetMapping("/chunks/{chunkId}")
    public ResponseEntity<byte[]> retrieveChunk(@PathVariable String chunkId) {
        try {
            InputStream inputStream = storageService.retrieveChunk(chunkId);
            byte[] chunkData = inputStream.readAllBytes();
            return ResponseEntity.ok(chunkData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/chunks/{chunkId}")
    public ResponseEntity<String> deleteChunk(@PathVariable String chunkId) {
        try {
            storageService.deleteChunk(chunkId);
            return ResponseEntity.ok("Chunk deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete chunk: " + e.getMessage());
        }
    }
}