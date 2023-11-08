package com.imp.checklist;

import com.imp.checklist.question.Question;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@Document("checklist-s")
public class StandardChecklist implements Checklist{
    @Id
    private java.lang.String id;
    private ArrayList<String> artifactTypes;
    private ArrayList<Question> questions;
}
