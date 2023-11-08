package com.imp.checklist;

import com.imp.checklist.question.Question;

import java.util.ArrayList;

public record ChecklistDTO(ArrayList<Checklist> checklistArrayList, ArrayList<Question> questions) {
}
