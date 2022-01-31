package com.answer.best.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.answer.best.entity.Questions;

@Repository
public interface QuestionRepo extends JpaRepository<Questions,Integer> {
	
	@Query(value="select * from questions",nativeQuery=true)
    List<Questions> getAllQuestions();

}
