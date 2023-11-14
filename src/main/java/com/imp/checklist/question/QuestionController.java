package com.imp.checklist.question;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/question")
public class QuestionController {

    private QuestionService questionService;

    @GetMapping("search")
    public QuestionResponse getQuestion(@RequestParam(name = "id") String id) {
        Question question = questionService.getQuestionById(id);

        return QuestionResponse.of(question);
    }
}
