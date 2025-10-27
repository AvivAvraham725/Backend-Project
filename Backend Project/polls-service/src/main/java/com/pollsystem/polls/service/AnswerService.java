package com.pollsystem.polls.service;

import com.pollsystem.polls.model.Answer;
import com.pollsystem.polls.model.Question;
import com.pollsystem.polls.repository.AnswerRepository;
import com.pollsystem.polls.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Answer createAnswer(Answer answer) {
        Long questionId = answer.getQuestion().getId();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestion_Id(questionId);
    }

    public List<Answer> getAnswersByUserId(Long userId) {
        return answerRepository.findByUserId(userId);
    }

    public void deleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }
}


