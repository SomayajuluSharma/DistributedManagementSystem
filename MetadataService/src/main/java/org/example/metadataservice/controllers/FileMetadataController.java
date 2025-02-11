package org.example.metadataservice.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.example.metadataservice.model.ChunkMetadata;
import org.example.metadataservice.model.FileMetadata;
import org.example.metadataservice.service.FileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metadata")
public class FileMetadataController {

    @Autowired
    private FileMetadataService service;

    @PostMapping("/files")
    public ResponseEntity<FileMetadata> initializeFileMetadata(@Valid @RequestBody FileMetadata fileMetadata){
        return ResponseEntity.ok(service.initializeFileMetadata(fileMetadata));
    }

    @PostMapping("/files/{fileId}/chunks")
    public ResponseEntity<FileMetadata> addChunkMetadata(@PathVariable String fileId, @Valid @RequestBody ChunkMetadata chunkMetadata){
        return ResponseEntity.ok(service.addChunkMetadata(fileId,chunkMetadata));
    }
    @PutMapping("/files/{fileId}/status")
    public ResponseEntity<FileMetadata> updateFileStatus(@PathVariable String fileId, @RequestParam String status){
        return ResponseEntity.ok(service.updateFileStatus(fileId, status));
    }
    @GetMapping("/files/{fileId}")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable String fileId) {
        return ResponseEntity.ok(service.getFileMetadata(fileId));
    }

    @GetMapping("/files/user/{uploadedBy}")
    public ResponseEntity<Page<FileMetadata>> getFilesByUser(
            @PathVariable String uploadedBy,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(service.getFilesByUser(uploadedBy, pageable));
    }

    @GetMapping("/files/status/{status}")
    public ResponseEntity<List<FileMetadata>> getFilesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getFilesByStatus(status));
    }

    @GetMapping("/files/missing-chunks")
    public ResponseEntity<List<FileMetadata>> getFilesWithMissingChunks() {
        return ResponseEntity.ok(service.getFilesWithMissingChunks());
    }
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Void> deleteFileMetadata(@PathVariable String fileId) {
        service.deleteFileMetadata(fileId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/files/{fileId}/chunks/{chunkId}/status")
    public ResponseEntity<String> updateChunkStatus(
            @PathVariable String fileId,
            @PathVariable String chunkId,
            @RequestParam String status) {

        boolean updated = service.updateChunkStatus(fileId, chunkId, status);
        if (updated) {
            return ResponseEntity.ok("Chunk status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File or chunk not found.");
        }
    }
}
