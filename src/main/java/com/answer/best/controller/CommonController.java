package com.answer.best.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.answer.best.dao.ScoreDaoImpl;
import com.answer.best.message.MessageStore;
import com.answer.best.repository.QuestionRepository;
import com.answer.best.repository.ScoreRepository;
import com.answer.best.request.RequestVO;
import com.answer.best.response.BaseClass;
import com.answer.best.response.ResponseVo;

@RestController
public class CommonController extends BaseClass {

	@Autowired
	ScoreRepository scoreRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	ScoreDaoImpl scoreDaoImpl;

	@GetMapping()
	public String check() {
		return "welcome";
	}

	@RequestMapping(value = "/allQuestion", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllQuestion() {
		return questionRepository.getAllQuestion();
	}

	@RequestMapping(value = "/scoreCount", method = RequestMethod.POST)
	public Map<String, Object> postUserScoreCount(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "COUNT  USER SCORE");
		response.put("response", "");
		if (userId > 0) {
			response = scoreDaoImpl.userScoreCount(userId);
		} else {
			response.put("response_code", "1");
			response.put("response_message", "Invalid userId");
		}
		return response;
	}

	@RequestMapping(value = "/getUserAnswer", method = RequestMethod.GET)
	public Map<String, Object> getAllUserAnswer(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "GET ALL USER ANSWER");
		response.put("response", "");
		response = scoreDaoImpl.getUserAnswer(userId);
		return response;
	}


	@RequestMapping(value = "/getUserScore", method = RequestMethod.GET)
	public Map<String, Object> getUserScore(int userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("method_name", "GET ALL USER ANSWER");
		response.put("response", "");
		response = scoreDaoImpl.getUserScoreById(userId);
		return response;
	}

	@RequestMapping(value = "/questions", method = RequestMethod.GET)
	public ResponseVo getQuestion() {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.getAllQuestions(), MessageStore.ALL_QUESTIONS);
	}

	@RequestMapping(value = "/user/answer/score", method = RequestMethod.GET)
	public ResponseVo userAnswer(@RequestParam final String email) throws SQLException {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.getUserAnswers(email), MessageStore.USER_ANSWER);
	}

	@RequestMapping(value = "/user/answer", method = RequestMethod.POST)
	public ResponseVo postAnswer(@RequestBody RequestVO requestVo) throws SQLException {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.storeAns(requestVo), MessageStore.POST_ANSWER);
	}
	
}
