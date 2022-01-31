package com.answer.best.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ScoreRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getUserAnswer(String query,int userId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query,userId);
		return rows;
	}

	public String getAnswer(String query1) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(query1, String.class);
	}
	
	public void insert(int userId,int score) {
		String sql="INSERT INTO score (user_id,score) VALUES(?,?)";
		jdbcTemplate.update(sql,userId,score);
	}
	
	public List<Map<String, Object>> getUserAnswer(int userId){
		return jdbcTemplate.queryForList("select ua.question_id ,q.question,ua.user_answer,q.answer from user_ansewer ua left  join question q on ua.question_id =q.question_id where ua.user_id ="+userId);
	}
	
	public List<Map<String, Object>> getUserByEmail(String query,String email){
		return jdbcTemplate.queryForList(query,email);
	}
	
	public int getUserId(String query) {
		return jdbcTemplate.queryForObject(query,Integer.class);
	}
	public int getUserScore(String query){
		return jdbcTemplate.queryForObject(query,Integer.class);
	}
	public List<Map<String, Object>> getUserScorebyId(String query){
		return jdbcTemplate.queryForList(query);
	}
	
	public int score(String query) {
		return jdbcTemplate.queryForObject( query,Integer.class);
	}
}
	
