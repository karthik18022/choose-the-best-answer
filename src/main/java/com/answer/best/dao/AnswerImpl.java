package com.answer.best.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.answer.best.config.SecurityConfig;
import com.answer.best.entity.Questions;
import com.answer.best.entity.Score;
import com.answer.best.entity.User;
import com.answer.best.entity.UserAnswer;
import com.answer.best.exception.ValidatationExcption;
import com.answer.best.model.JwtTokenUtilV1;
import com.answer.best.repository.AnswerRepo;
import com.answer.best.repository.ScoreRepo;
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
	SecurityConfig config;

	@Autowired
	JwtTokenUtilV1 jwtTokenUtilV1;

	@Transactional
	public RequestVO postAnswer(RequestVO request) {

		try {
			List<UserAnswer> answerList = new ArrayList<>();
			User user = new User();
			boolean valid = this.validateEmail(request.getEmail());
			if (valid == false) {
				throw new ValidationException("email is not valid");
			}
				user.setEmail(request.getEmail());
				user.setUserName(request.getUsername());
				String password = config.passwordEncoder().encode(request.getPassword());
				user.setPassword(password);
				if (user.getEmail() != null) {
					userRepo.save(user);
					User userObjV1 = userRepo.findByEmail(request.getEmail());
					for (QuestionRequest questionRequest : request.getRequest()) {
						UserAnswer userAnswer = new UserAnswer();
						String userAns = questionRequest.getUserAnswer();
						Questions question = new Questions();
						question.setQuestionId(questionRequest.getQuestionId());
						userAnswer.setAnswer(userAns);
						userAnswer.setQuestion(question);
						userAnswer.setUser(userObjV1);
						answerList.add(userAnswer);
					}
					answerRepo.saveAll(answerList);
					int score = scoreRepo.getScore(userObjV1.getUserId());
					Score scoreObj = new Score();
					scoreObj.setScore(score);
					scoreObj.setUser(userObjV1);
					scoreRepo.save(scoreObj);
				}
		} catch (ValidatationExcption e) {
			 new ValidatationExcption("email is not valid");
		}

		return request;
	}

	public ScoreAndAnswerVO getAllAnswer(String email) {
		final List<UserAnswerVo> responseList = new ArrayList<>();
		ScoreAndAnswerVO params = new ScoreAndAnswerVO();
		User user = userRepo.findByEmail(email);
		int userId = userRepo.getUserId(user.getEmail());
		List<UserAnswer> queryList = uaRepo.getUserAns(userId);
		for (UserAnswer userAnswer : queryList) {
			UserAnswerVo userAnswerObj = new UserAnswerVo();
			userAnswerObj.setQuestionId(userAnswer.getQuestion().getQuestionId());
			userAnswerObj.setQuestion(userAnswer.getQuestion().getQuestion());
			userAnswerObj.setUserAnswer(userAnswer.getAnswer());
			userAnswerObj.setAnswer(userAnswer.getQuestion().getAnswer());
			responseList.add(userAnswerObj);
		}
		int score = scoreRepo.score(userId);
		params.setUserId(userId);
		params.setScore(score);
		params.setUserAnswerVo(responseList);
		return params;
	}

	public static boolean validateEmail(String email) {
		String emailReg = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailReg);
		if (pattern.matcher(email).matches()) {
			return true;
		} else {
			return false;
		}
	}

}
