package com.pollsystem.polls.controller;

import com.pollsystem.polls.model.Answer;
import com.pollsystem.polls.model.Question;
import com.pollsystem.polls.repository.AnswerRepository;
import com.pollsystem.polls.repository.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public StatisticsController(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        List<Question> questions = questionRepository.findAll();
        List<Answer> answers = answerRepository.findAll();

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalQuestions", questions.size());
        stats.put("totalAnswers", answers.size());

        Map<Long, Long> answersByQuestion = new HashMap<>();
        for (Answer a : answers) {
            Long qId = (a.getQuestion() != null) ? a.getQuestion().getId() : null;
            if (qId != null) {
                answersByQuestion.put(qId, answersByQuestion.getOrDefault(qId, 0L) + 1);
            }
        }
        stats.put("answersByQuestion", answersByQuestion);

        Map<Long, Long> answersByUser = new HashMap<>();
        for (Answer a : answers) {
            Long uId = a.getUserId();
            answersByUser.put(uId, answersByUser.getOrDefault(uId, 0L) + 1);
        }
        stats.put("answersByUser", answersByUser);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Map<String, Object>> getStatsByQuestion(@PathVariable Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }

        List<Answer> answers = answerRepository.findAll().stream()
                .filter(a -> a.getQuestion() != null && a.getQuestion().getId().equals(id))
                .collect(Collectors.toList());

        Map<String, Long> counts = new HashMap<>();
        for (Answer a : answers) {
            String option = a.getSelectedOption();
            counts.put(option, counts.getOrDefault(option, 0L) + 1);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("questionId", id);
        response.put("questionText", question.getQuestionText());
        response.put("totalAnswers", answers.size());
        response.put("answers", counts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getStatsByUser(@PathVariable Long userId) {
        List<Answer> answers = answerRepository.findAll().stream()
                .filter(a -> a.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<Map<String, String>> userAnswers = new ArrayList<>();
        for (Answer a : answers) {
            Map<String, String> entry = new HashMap<>();
            entry.put("question", a.getQuestion().getQuestionText());
            entry.put("selectedOption", a.getSelectedOption());
            userAnswers.add(entry);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("userId", userId);
        response.put("totalAnswers", userAnswers.size());
        response.put("answers", userAnswers);

        return ResponseEntity.ok(response);
    }
}
