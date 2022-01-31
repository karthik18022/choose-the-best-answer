package com.answer.best.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.answer.best.entity.Questions;
import com.answer.best.entity.Score;
import com.answer.best.entity.User;
import com.answer.best.entity.UserAnswer;
import com.answer.best.repository.AnswerRepo;
import com.answer.best.repository.ScoreRepo;
import com.answer.best.repository.UserAnswerRepo;
import com.answer.best.repository.UserRepo;
import com.answer.best.request.QuestionRequest;
import com.answer.best.request.RequestVO;
import com.answer.best.response.ScoreAndAnswerVO;
import com.answer.best.response.UserAnswerVo;

@Service
public class AnswerImpl {
	org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AnswerImpl.class);
	@Autowired
	UserRepo userRepo;
	@Autowired
	ScoreRepo scoreRepo;

	@Autowired
	AnswerRepo answerRepo;

	@Autowired
	UserAnswerRepo uarepo;

	public RequestVO postAnswer(RequestVO request) {
		try {
			List<UserAnswer> answerList = new ArrayList<>();
			User user = new User();
			user.setEmail(request.getEmail());
			userRepo.save(user);
			int userId = userRepo.getUserId(request.getEmail());
			User userObj = new User();
			userObj.setUserId(userId);
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
			int score = scoreRepo.getScore(userId);
			Score scoreObj = new Score();
			scoreObj.setScore(score);
			scoreObj.setUser(userObj);
			scoreRepo.save(scoreObj);
		} catch (Exception e) {
			logger.info("Error in exception", e);
		}
		return request;
	}

	public ScoreAndAnswerVO getAllAnswer(String email) {

		final List<UserAnswerVo> responseList = new ArrayList<>();
		ScoreAndAnswerVO params = new ScoreAndAnswerVO();
		int userId = userRepo.getUserId(email);
		try {
			List<UserAnswer> queryList = uarepo.getUserAns(userId);
			if (queryList != null) {
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
			}
		} catch (Exception e) {
			logger.info("Error in exception", e);
		}
		return params;
	}
}
