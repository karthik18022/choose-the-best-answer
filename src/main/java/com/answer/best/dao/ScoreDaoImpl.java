package com.answer.best.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.answer.best.repository.QuestionRepository;
import com.answer.best.repository.ScoreRepository;
import com.answer.best.request.QuestionRequest;
import com.answer.best.request.RequestVO;
import com.answer.best.response.QuestionResponse;
import com.answer.best.response.ScoreAndAnswerVO;
import com.answer.best.response.ScoreResponse;
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

	public Map<String, Object> userScoreCount(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "COUNT  USER SCORE");
		int score = 0;
		List<Map<String, Object>> userAnswerList = new LinkedList<>();
		if (userId > 0) {
			String query = "SELECT ua.*,qa.answer FROM user_ansewer ua LEFT OUTER JOIN question qa ON ua.question_id=qa.question_id WHERE user_id=?";
			userAnswerList = scoreRepositroy.getUserAnswer(query, userId);
			if (userAnswerList != null) {
				for (int i = 0; i < userAnswerList.size(); i++) {
					Map<String, Object> listItem = userAnswerList.get(i);
					String answerList = (String) listItem.get("answer");
					String userAns = (String) listItem.get("user_answer");
					boolean compareTwoAns = answerList.equalsIgnoreCase(userAns);
					if (compareTwoAns == true) {
						score++;
					}
				}
			}
			scoreRepositroy.insert(userId, score);
			response.put("response_code", "0");
			response.put("response_message", "success");
			response.put("score", score);
		} else {
			response.put("response_code", "1");
			response.put("response_message", "user does not exist");
		}

		return response;
	}

	public Map<String, Object> getUserAnswer(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "GET  USER ANSWER");
		response.put("response", "");
		List<Map<String, Object>> finalList = new LinkedList<>();
		String query = "select ua.question_id ,q.question,ua.user_answer,q.answer from `local`.user_ansewer ua left outer join local.question q on ua .question_id =q.question_id where  ua.user_id ="
				+ userId;
		finalList = scoreRepositroy.getUserAnswer(query);
		if (finalList != null) {
			response.put("response_code", "0");
			response.put("response_message", "success");
			response.put("response", finalList);
		} else {
			response.put("response_code", "1");
			response.put("response_message", "No Record");
		}
		return response;
	}

	public ScoreResponse postUserAnswer(String email) throws Exception, IOException, ParseException {
		ScoreResponse response = new ScoreResponse();
		JSONParser jsonParser = new JSONParser();
		Connection con = dataSource.getConnection();
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
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("D:/insert.json"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("answers_data");
		PreparedStatement pstmt = con
				.prepareStatement("INSERT INTO user_ansewer(user_id,question_id,user_answer) values (?, ?, ? )");
		for (Object object : jsonArray) {
			JSONObject record = (JSONObject) object;
			int user_id = userId;
			int question_id = Integer.parseInt((String) record.get("question_id"));
			String user_answer = (String) record.get("user_answer");
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, question_id);
			pstmt.setString(3, user_answer);
			pstmt.executeUpdate();
//	            compare two strings
			String answerByQuestion = jdbcTemplate
					.queryForObject("SELECT answer FROM question q where q.question_id=" + question_id, String.class);
			boolean compareTwoAns = answerByQuestion.equalsIgnoreCase(user_answer);
			if (compareTwoAns == true) {
				score++;
			}
		}
		scoreRepositroy.insert(userId, score);
		response.setScore(score);
		return response;
	}

	public Map<String, Object> getUserScoreById(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "GET ALL USER ANSWER");
		response.put("response", "");
		List<Map<String, Object>> finalList = new LinkedList<>();
		String query = "select score from score where user_id= " + userId;
		finalList = scoreRepositroy.getUserScorebyId(query);
		if (finalList != null) {
			response.put("response_code", "0");
			response.put("response_message", "success");
			response.put("response", finalList);
		} else {
			response.put("response_code", "1");
			response.put("response_message", "No Record");
		}
		return response;
	}

	public ScoreResponse getUserScore(int userId) {
		String query = "select score from score where user_id= " + userId;
		int finalList = scoreRepositroy.getUserScore(query);
		ScoreResponse response = new ScoreResponse();
		response.setScore(finalList);
		return response;
	}

	// request and response from POJO classes

	public ScoreAndAnswerVO getUserAnswers(String email) throws SQLException {
		List<Map<String, Object>> answerList = new LinkedList<>();
		final List<UserAnswerVo> responseList = new ArrayList<>();
		Connection con = dataSource.getConnection();
		ScoreAndAnswerVO params = new ScoreAndAnswerVO();
		int userId = 0;
		String sql = "SELECT u.user_id" + " FROM user u" + " WHERE u.email= ?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					userId = rs.getInt(1);
				}
			}
		}
		String query = "select ua.question_id ,q.question,ua.user_answer,q.answer from user_ansewer ua left  join question q on ua.question_id =q.question_id where ua.user_id ="
				+ userId;
		answerList = scoreRepositroy.getUserAnswer(query);
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
			int score = jdbcTemplate.queryForObject("SELECT score FROM score where user_id=" + userId, Integer.class);
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
		ResultSet rs = stmt.executeQuery("SELECT last_insert_id()");
		if (rs.next()) {
			userId = rs.getInt(1);
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
		String query = "select sum( case when q.answer =ua.user_answer then 1 else 0 end )as score from user_ansewer ua left join question q on ua.question_id = q.question_id  where ua.user_id ="
				+ userId;
		score = scoreRepositroy.score(query);
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