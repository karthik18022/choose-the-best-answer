package com.answer.best.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.answer.best.config.SecurityConfig;
import com.answer.best.entity.Questions;
import com.answer.best.entity.Score;
import com.answer.best.entity.Test;
import com.answer.best.entity.User;
import com.answer.best.entity.UserAnswer;
import com.answer.best.exception.ValidatationExcption;
import com.answer.best.message.MessageStore;
import com.answer.best.model.JwtTokenUtilV1;
import com.answer.best.repository.AnswerRepo;
import com.answer.best.repository.ScoreRepo;
import com.answer.best.repository.TestRepo;
import com.answer.best.repository.UserAnswerRepo;
import com.answer.best.repository.UserRepo;
import com.answer.best.request.QuestionRequest;
import com.answer.best.request.RequestVO;
import com.answer.best.response.ScoreAndAnswerVO;
import com.answer.best.response.UserAnswerVo;

@Service
@Component
public class AnswerImpl {
	org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AnswerImpl.class);

	@Autowired
	UserRepo userRepo;

	@Autowired
	ScoreRepo scoreRepo;

	@Autowired
	AnswerRepo answerRepo;

	@Autowired
	UserAnswerRepo uaRepo;

	@Autowired
	TestRepo testRepo;

	@Autowired
	SecurityConfig config;

	@Autowired
	JwtTokenUtilV1 jwtTokenUtilV1;

	@Transactional
	public RequestVO postAnswer(RequestVO request) {
		try {
			List<UserAnswer> answerList = new ArrayList<>();
			User user = new User();
			user.setEmail(request.getEmail());
			user.setUserName(request.getUsername());
			String password = config.passwordEncoder().encode(request.getPassword());
			user.setPassword(password);
			if(user.getEmail() != null) {
			userRepo.save(user);
			User userObjV1 = userRepo.findByEmail(request.getEmail());
			User userObj = new User();
			userObj.setUserId(userObjV1.getUserId());
			for (int i = 0; i < request.getRequest().size(); i++) {
				QuestionRequest listItem = request.getRequest().get(i);
				String userAnswer = listItem.userAnswer;
				UserAnswer userAns = new UserAnswer();
				Questions questionObj = new Questions();
				questionObj.setQuestionId(listItem.questionId);
				userAns.setUser(userObj);
				userAns.setQuestion(questionObj);
				userAns.setAnswer(userAnswer);
				answerList.add(userAns);
			}
			answerRepo.saveAll(answerList);
			int score = scoreRepo.getScore(userObjV1.getUserId());
			Score scoreObj = new Score();
			scoreObj.setScore(score);
			scoreObj.setUser(userObj);
			scoreRepo.save(scoreObj);
			}
		}
		catch (ValidatationExcption e) {
			throw new ValidatationExcption(MessageStore.EMAIL_FOUND_EXCEPTION);
		}
		return request;

	}

	public ScoreAndAnswerVO getAllAnswer(String email) {
		final List<UserAnswerVo> responseList = new ArrayList<>();
		ScoreAndAnswerVO params = new ScoreAndAnswerVO();
//		String jwtToken = token.substring(7);
//		String email=jwtTokenUtilV1.getUsernameFromToken(jwtToken);
		User user = userRepo.findByEmail(email);
		if (user == null) {
			throw new ValidatationExcption(MessageStore.EMAIL_NOT_FOUND_EXCEPTION);
		} else {
			int userId = userRepo.getUserId(user.getEmail());
			List<UserAnswer> queryList = uaRepo.getUserAns(userId);
			for (int i = 0; i < queryList.size(); i++) {
				UserAnswerVo userAnswerObj = new UserAnswerVo();
				userAnswerObj.setQuestionId(queryList.get(i).getQuestion().getQuestionId());
				userAnswerObj.setQuestion(queryList.get(i).getQuestion().getQuestion());
				userAnswerObj.setUserAnswer(queryList.get(i).getAnswer());
				userAnswerObj.setAnswer(queryList.get(i).getAnswer());
				responseList.add(userAnswerObj);
			}
			int score = scoreRepo.score(userId);
			params.setUserId(userId);
			params.setScore(score);
			params.setUserAnswerVo(responseList);
			return params;
		}
	}

	public Test postTest(Test test) {
		Test testObj = new Test();
		testObj.setName(test.getName());
		testObj.setTest(test.getTest());
		testRepo.save(testObj);
		return testObj;
	}

}
