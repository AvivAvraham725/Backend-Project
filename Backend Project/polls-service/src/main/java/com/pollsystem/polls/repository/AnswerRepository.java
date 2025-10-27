package com.pollsystem.polls.repository;

import com.pollsystem.polls.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion_Id(Long questionId);

    List<Answer> findByUserId(Long userId);
}
