package org.example.metadataservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.example.metadataservice.model.ChunkMetadata;

@Getter
@Setter
public class FileMetadata{
    @Id
    private String fileId;
    private String fileName;
    private String contentType;
    private long fileSize;
    private LocalDateTime createdAt;
    private String uploadedBy;
    private List<ChunkMetadata> chunks = new ArrayList<>();
    private String status;
    private String errorMessage;
    public FileMetadata() {
        this.status = "UPLOADING";
        this.createdAt = LocalDateTime.now();
    }

    public void addChunk(ChunkMetadata chunk) {
        this.chunks.add(chunk);
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
