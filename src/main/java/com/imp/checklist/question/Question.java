package com.imp.checklist.question;

import com.imp.checklist.Severity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("questions")
public class Question {
    @Id
    private String id;
    private String question;
    private String artifactTypeId;
    private String severity;
    private boolean answer;
    private int quantity;
    private String observation;
    private String defectTypeId;
}
