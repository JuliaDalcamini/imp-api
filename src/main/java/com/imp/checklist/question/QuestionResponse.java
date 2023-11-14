package com.imp.checklist.question;

import com.imp.checklist.Severity;

public record QuestionResponse(
        String id,
        String question,
        String artifactTypeId,
        String severity,
        boolean answer,
        int quantity,
        String observation,
        String defectType
) {

    static QuestionResponse of(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestion(),
                question.getArtifactTypeId(),
                question.getSeverity(),
                question.isAnswer(),
                question.getQuantity(),
                question.getObservation(),
                question.getDefectTypeId()
        );
    }
}