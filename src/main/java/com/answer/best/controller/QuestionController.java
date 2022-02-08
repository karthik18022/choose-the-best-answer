package com.answer.best.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.answer.best.dao.AnswerImpl;
import com.answer.best.dao.QuestionImpl;
import com.answer.best.entity.Questions;
import com.answer.best.entity.Test;
import com.answer.best.message.MessageStore;
import com.answer.best.request.RequestVO;
import com.answer.best.response.BaseClass;
import com.answer.best.response.ResponseVo;

@RestController
public class QuestionController extends BaseClass {
	
	@Autowired
	QuestionImpl questionImpl;
	
	@Autowired
	AnswerImpl answerImpl;
	
	@RequestMapping(value="/add/question",method=RequestMethod.POST)
	public ResponseVo addQuestion(@RequestBody Questions questions) {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.questionImpl.addQuestion(questions), MessageStore.POST_QUESTION);
	}
	
	@RequestMapping(value="/qestionsV1",method=RequestMethod.GET)
	public ResponseVo getAllQuestions() {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.questionImpl.getQuestions(), MessageStore.ALL_QUESTIONS);
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public ResponseVo postAnswer( @RequestBody RequestVO request) {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.answerImpl.postAnswer(request), MessageStore.POST_ANSWER);
	}
	
	@RequestMapping(value="/user/answers",method=RequestMethod.GET)
	public ResponseVo getAnswers(Principal principal) {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.answerImpl.getAllAnswer(principal.getName()), MessageStore.USER_ANSWER);
	}
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public ResponseVo postTest(@RequestBody Test test) {
		final ResponseVo responseDao = new ResponseVo();
		return super.success(responseDao, this.answerImpl.postTest(test), MessageStore.USER_ANSWER);
	}
	
	@RequestMapping(value="/mapping",method=RequestMethod.GET)
	public List<Object> getTest(Authentication auth){
		List<Object> list=new ArrayList<>();
		Object name= auth.getDetails();
		list.add(name);
		Object test=auth.getPrincipal();
		list.add(test);
		Object sub=auth.getCredentials();
		Object authorities=auth.getAuthorities();
		list.add(sub);
		list.add(authorities);
		return list;
	}
	
}
