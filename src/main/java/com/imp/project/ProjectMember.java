package com.imp.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class ProjectMember {
    @Id
    private String id;
    private String userId;
    private String projectId;
    private ProjectRole role;
}
