package org.example.metadataservice.repository;

import java.util.List;

import org.example.metadataservice.model.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String>{

    // Find by uploadedBy
    //List<FileMetadata> findByUploadedBy(String uploadedBy);
    Page<FileMetadata> findByUploadedBy(String uploadedBy, Pageable pageable);
    // Find by status
    List<FileMetadata> findByStatus(String status);

    List<FileMetadata> findByChunksStatus(String status);

}
