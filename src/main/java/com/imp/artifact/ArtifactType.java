package com.imp.artifact;

import com.imp.checklist.question.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
@Document("artifactType")
public class ArtifactType {
    @Id
    private String id;
    private String name;
    private ArrayList<Question> questions;
}
