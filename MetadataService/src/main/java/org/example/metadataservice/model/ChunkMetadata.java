package org.example.metadataservice.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Getter
@Setter
public class ChunkMetadata {
    @NotNull
    private String chunkId;

    private int sequenceNumber;

    private String storageNodeId;

    private long size;

    private String checkSum;

    private String status;

    public ChunkMetadata() {
        this.status = "UPLOADING";
    }
}
