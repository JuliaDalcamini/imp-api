package com.imp.artifact;

import com.imp.appUser.AppUser;
import com.imp.project.Project;

import java.time.LocalDate;

public record ArtifactResponse(
    String id,
    String name,
    LocalDate creationDate,
    String creatorName
) {

    static ArtifactResponse of(Artifact artifact, AppUser creator) {
        return new ArtifactResponse(
            artifact.getId(),
            artifact.getName(),
            artifact.getCreationDateTime().toLocalDate(),
            creator.getFullName()
        );
    }
}
