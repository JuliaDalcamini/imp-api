package com.imp.artifact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Document("artifacts")
public class Artifact {
    @Id
    private String id;
    private String name;
    private String artifactType;
    private String creatorId;
    private boolean status;
    private LocalDateTime creationDateTime;
    private LocalDateTime conclusionDateTime;

    public Artifact(String name, String artifactType, String creatorId, boolean status, LocalDateTime creationDateTime, LocalDateTime conclusionDateTime) {
        this.name = name;
        this.artifactType = artifactType;
        this.creatorId = creatorId;
        this.status = status;
        this.creationDateTime = creationDateTime;
        this.conclusionDateTime = conclusionDateTime;
    }
}
