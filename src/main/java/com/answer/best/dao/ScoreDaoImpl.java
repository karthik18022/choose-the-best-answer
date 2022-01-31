package com.answer.best.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.answer.best.repository.QuestionRepository;
import com.answer.best.repository.ScoreRepository;
import com.answer.best.request.QuestionRequest;
import com.answer.best.request.RequestVO;
import com.answer.best.response.QuestionResponse;
import com.answer.best.response.ScoreAndAnswerVO;
import com.answer.best.response.UserAnswerVo;

@Service
public class ScoreDaoImpl {

	@Autowired
	ScoreRepository scoreRepositroy;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Autowired
	QuestionRepository questionRepository;

	// request and response from POJO classes

	public ScoreAndAnswerVO getUserAnswers(String email) throws SQLException {
		List<Map<String, Object>> answerList = new LinkedList<>();
		final List<UserAnswerVo> responseList = new ArrayList<>();
		Connection con = dataSource.getConnection();
		ScoreAndAnswerVO params = new ScoreAndAnswerVO();
		int userId = 0;
		int score = 0;
		String sql = "SELECT u.user_id" + " FROM user u" + " WHERE u.email= ?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					userId = rs.getInt(1);
				}
			}
		}
		answerList = scoreRepositroy.getUserAnswer(userId);
		if (answerList != null) {
			for (int i = 0; i < answerList.size(); i++) {
				Map<String, Object> listItem = answerList.get(i);
				UserAnswerVo response = new UserAnswerVo();
				response.setQuestionId((int) listItem.get("question_id"));
				response.setQuestion((String) listItem.get("question"));
				response.setUserAnswer((String) listItem.get("user_answer"));
				response.setAnswer((String) listItem.get("answer"));
				responseList.add(response);
			}
			String query = "SELECT score FROM score where user_id=?";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, userId);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						score = rs.getInt(1);
					}
				}
			}
			params.setScore(score);
			params.setUserId(userId);
			params.setUserAnswerVo(responseList);
		}
		return params;
	}

	public RequestVO storeAns(RequestVO requestVo) throws SQLException {
		int userId = 0;
		int score = 0;
		Connection con = dataSource.getConnection();
		String sql = "INSERT INTO user(email) values(?) ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, requestVo.getEmail());
		stmt.executeUpdate();
		String user = "SELECT u.user_id FROM user u  WHERE u.email= ?";
		try (PreparedStatement stmt1 = con.prepareStatement(user)) {
			stmt1.setString(1, requestVo.getEmail());
			try (ResultSet rs = stmt1.executeQuery()) {
				while (rs.next()) {
					userId = rs.getInt(1);
				}
			}
		}
		PreparedStatement pstmt = con
				.prepareStatement("INSERT INTO user_ansewer(user_id,question_id,user_answer) values (?, ?, ? )");
		for (int i = 0; i < requestVo.getRequest().size(); i++) {
			QuestionRequest listItem = requestVo.getRequest().get(i);
			int questionId = listItem.questionId;
			String userAnswer = listItem.userAnswer;
			pstmt.setInt(1, userId);
			pstmt.setInt(2, (int) questionId);
			pstmt.setString(3, (String) userAnswer);
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		String query = "select sum( case when q.answer =ua.user_answer then 1 else 0 end )as score from user_ansewer ua left join question q on ua.question_id = q.question_id  where ua.user_id =?";
		try (PreparedStatement stmt1 = con.prepareStatement(query)) {
			stmt1.setInt(1, userId);
			try (ResultSet rs = stmt1.executeQuery()) {
				while (rs.next()) {
					score = rs.getInt(1);
				}
			}
		}
		scoreRepositroy.insert(userId, score);
		return requestVo;
	}

	public List<QuestionResponse> getAllQuestions() {
		List<Map<String, Object>> questionList = questionRepository.getAllQuestion();
		final List<QuestionResponse> finalList = new ArrayList<>();
		if (questionList != null) {
			for (int i = 0; i < questionList.size(); i++) {
				Map<String, Object> listItem = questionList.get(i);
				QuestionResponse response = new QuestionResponse();
				response.setQuestionId((int) listItem.get("question_id"));
				response.setQuestion((String) listItem.get("question"));
				response.setOptionA((String) listItem.get("option_a"));
				response.setOptionB((String) listItem.get("option_b"));
				response.setOptionC((String) listItem.get("option_c"));
				response.setOptionD((String) listItem.get("option_d"));
				finalList.add(response);
			}
		}
		return finalList;

	}
	
	

}