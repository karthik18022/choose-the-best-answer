package com.answer.best.repository;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepository {

	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getAllQuestion(){
		List<Map<String, Object>> questionList = jdbcTemplate.queryForList("select question_id, question,option_a,option_b,option_c,option_d from  question");
		return questionList;
	}
}
