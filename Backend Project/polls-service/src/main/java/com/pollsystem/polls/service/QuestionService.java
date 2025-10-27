package com.pollsystem.polls.service;

import com.pollsystem.polls.model.Question;
import com.pollsystem.polls.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public void deleteQuestionsByUserId(Long userId) {
        List<Question> userQuestions = questionRepository.findByUserId(userId);
        questionRepository.deleteAll(userQuestions);
        System.out.println(" Deleted all questions for user ID " + userId);
    }
}

