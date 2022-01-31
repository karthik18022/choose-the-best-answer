package com.answer.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.answer.best.entity.UserAnswer;

@Repository
public interface AnswerRepo extends JpaRepository<UserAnswer, Integer> {
	

}
