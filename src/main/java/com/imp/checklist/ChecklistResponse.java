package com.imp.checklist;

import com.imp.appUser.AppUser;
import com.imp.checklist.question.Question;

import java.util.ArrayList;

public record ChecklistResponse(
        String id,
        ArrayList<String> artifactTypes,
        ArrayList<Question> questions
) {

    static ChecklistResponse of(EmptyChecklist checklist, AppUser creator) {
        return new ChecklistResponse(
                checklist.getId(),
                checklist.getArtifactTypes(),
                checklist.getQuestions()
        );
    }
}
