package org.example.metadataservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.metadataservice.exception.FileNotFoundException;
import org.example.metadataservice.model.ChunkMetadata;
import org.example.metadataservice.model.FileMetadata;
import org.example.metadataservice.repository.FileMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FileMetadataService {

    private static final Logger logger = LoggerFactory.getLogger(FileMetadataService.class);

    @Autowired
    private FileMetadataRepository repository;

    //intialize file
    public FileMetadata initializeFileMetadata(FileMetadata fileMetadata) {
        // Generate a unique fileId if not provided
        if (fileMetadata.getFileId() == null) {
            fileMetadata.setFileId(UUID.randomUUID().toString());
        }

        // Set default values
        fileMetadata.setCreatedAt(LocalDateTime.now());
        fileMetadata.setStatus("UPLOADING");

        // Save the file metadata
        return repository.save(fileMetadata);
    }
    //Add chunk metadata to an existing file.

    public FileMetadata addChunkMetadata(String fileId, ChunkMetadata chunkMetadata){
        FileMetadata fileMetadata = repository.findById(fileId).orElseThrow(()
                -> new RuntimeException("File not found"));
        fileMetadata.addChunk(chunkMetadata);
        return repository.save(fileMetadata);
    }
    //Update the status of a file (e.g., "UPLOADING", "COMPLETED", "FAILED").
    public FileMetadata updateFileStatus(String fileId, String status){
        FileMetadata fileMetadata = repository.findById(fileId).orElseThrow(()
                -> new FileNotFoundException("File not found with ID: " + fileId));
        fileMetadata.updateStatus(status);
        return repository.save(fileMetadata);
    }
    //Retrive metadata for a specific file
    public FileMetadata getFileMetadata(String fileId){
        return repository.findById(fileId).orElseThrow(() -> new FileNotFoundException("File not found with ID: " + fileId));
    }
    //Retrive all Metadata uploaded by specific user
    public Page<FileMetadata> getFilesByUser(String uploadedBy, Pageable pageable) {
        return repository.findByUploadedBy(uploadedBy, pageable);
    }
    // Retrieve all files with a specific status.
    public List<FileMetadata> getFilesByStatus(String status) {
        return repository.findByStatus(status);
    }
    // Retrieve all files with missing chunks.
    public List<FileMetadata> getFilesWithMissingChunks() {
        return repository.findByChunksStatus("FAILED");
    }
    public void deleteFileMetadata(String fileId) {
        if (!repository.existsById(fileId)) {
            throw new FileNotFoundException("File not found with ID: " + fileId);
        }
        repository.deleteById(fileId);
    }
    public boolean updateChunkStatus(String fileId, String chunkId, String status) {
        // Find the file metadata
        Optional<FileMetadata> optionalFileMetadata = repository.findById(fileId);

        if (optionalFileMetadata.isEmpty()) {
            throw new RuntimeException("File not found with ID: " + fileId);
        }

        FileMetadata fileMetadata = optionalFileMetadata.get();

        // Find the chunk in the file metadata
        for (ChunkMetadata chunk : fileMetadata.getChunks()) {
            if (chunk.getChunkId().equals(chunkId)) {
                chunk.setStatus(status); // Update status
                repository.save(fileMetadata); // Save changes
                return true;
            }
        }

        throw new RuntimeException("Chunk not found with ID: " + chunkId);
    }

    //
}
