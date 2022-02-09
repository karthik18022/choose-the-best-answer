package com.answer.best.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.answer.best.dao.ScoreDaoImpl;
import com.answer.best.message.MessageStore;
import com.answer.best.request.RequestVO;
import com.answer.best.response.BaseClass;
import com.answer.best.response.ResponseVo;

@RestController
public class CommonController extends BaseClass {

	@Autowired
	ScoreDaoImpl scoreDaoImpl;

	@RequestMapping(value = "/question", method = RequestMethod.GET)
	public ResponseVo getQuestion() {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.getAllQuestions(), MessageStore.ALL_QUESTIONS);
	}

	@RequestMapping(value = "/user/answer/score", method = RequestMethod.GET)
	public ResponseVo userAnswer(@RequestParam   final String email) throws SQLException {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.getUserAnswers(email), MessageStore.USER_ANSWER);
	}

	@RequestMapping(value = "/user/answer", method = RequestMethod.POST)
	public ResponseVo postAnswer(@RequestBody RequestVO requestVo) throws SQLException {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.scoreDaoImpl.storeAns(requestVo), MessageStore.POST_ANSWER);
	}

}
