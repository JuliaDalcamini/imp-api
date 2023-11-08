package com.imp.project;

import com.imp.appUser.AppUser;

import java.time.LocalDate;

public record ProjectResponse(
    String id,
    String name,
    LocalDate creationDate,
    String creatorName
) {

    static ProjectResponse of(Project project, AppUser creator) {
        return new ProjectResponse(
            project.getId(),
            project.getNameProject(),
            project.getCreationDateTime().toLocalDate(),
            creator.getFullName()
        );
    }
}
