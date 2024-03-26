package com.imp.project;

import com.imp.appUser.AppUserDTO;
import com.imp.artifact.Artifact;
import com.imp.checklist.Checklist;
import com.imp.priority.PriorityMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@Document("projects")
public class Project {
    @Id
    private String id;
    private String nameProject;
    private LocalDateTime creationDateTime;
    private String creatorId;
    private PriorityMethod prioritizationMethod;
    private Checklist checklist;
    private ArrayList<Artifact> artifactsList;
    private ArrayList<AppUserDTO> team;

    public Project() {
    }

    public Project(String nameProject, LocalDateTime creationDateTime, String creatorId, PriorityMethod prioritizationMethod, Checklist checklist, ArrayList<Artifact> artifactsList, ArrayList<AppUserDTO> team) {
        this.nameProject = nameProject;
        this.creationDateTime = creationDateTime;
        this.creatorId = creatorId;
        this.prioritizationMethod = prioritizationMethod;
        this.checklist = checklist;
        this.artifactsList = artifactsList;
        this.team = team;
    }
}
