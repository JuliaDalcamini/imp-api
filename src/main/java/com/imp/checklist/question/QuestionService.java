package com.imp.checklist.question;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    private boolean isQuestionExists(String id) {
        return questionRepository
                .findById(id)
                .isPresent();
    }

    public Question getQuestionById(String id) {
        if (isQuestionExists(id)) {
            return questionRepository.findById(id).get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pergunta não encontrada");
    }
}
