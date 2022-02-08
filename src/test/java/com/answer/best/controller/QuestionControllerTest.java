package com.answer.best.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.answer.best.dao.AnswerImpl;
import com.answer.best.dao.QuestionImpl;
import com.answer.best.entity.Questions;
import com.answer.best.entity.User;
import com.answer.best.repository.QuestionRepo;
import com.answer.best.repository.UserRepo;
import com.answer.best.request.QuestionRequest;
import com.answer.best.request.RequestVO;
import com.answer.best.response.QuestionResponse;


@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionControllerTest {

    @InjectMocks
	QuestionController questionController;
	
	@MockBean
	QuestionImpl questionImpl;
	
	@MockBean
	AnswerImpl answerImpl;
	
	@Mock
	QuestionRepo questionRepo;
	
	@Mock
	UserRepo userRepo;
	
	@Test
	public void test() {
		List<QuestionResponse> questionList=questionImpl.getQuestions();
		assertNotNull(questionList);
		}
	
	@Test
	public void addQuestion() {
		Questions question=new Questions();
		question.setQuestionId(11);
		question.setQuestion("test");
		question.setOptionA("test-1");
		question.setOptionB("test-2");
		question.setOptionC("test-3");
		question.setOptionD("test-4");
		question.setAnswer("answer");
		questionImpl.addQuestion(question);
		Mockito.verify(questionImpl,Mockito.times(1)).addQuestion(question);
		Assert.assertNotNull(question);
	}
	
	@Test
	public void addAnswer() {
		RequestVO req=new RequestVO();
		req.setEmail("test@gmail.com");
		req.setPassword("test");
	    List<QuestionRequest> list=new ArrayList<>();
	    QuestionRequest qRequest=new QuestionRequest();
	    qRequest.setQuestionId(1);
	    qRequest.setUserAnswer("gandhi");
	    list.add(qRequest);
	    qRequest.setQuestionId(92);
	    qRequest.setUserAnswer("sachin");
	    list.add(qRequest);
	    qRequest.setQuestionId(93);
	    qRequest.setUserAnswer("cpu");
	    list.add(qRequest);
	    req.setRequest(list);
	    req.setUsername("test");
	    answerImpl.postAnswer(req);
	    Mockito.verify(answerImpl,Mockito.times(1)).postAnswer(req);
	    Assert.assertNotNull(req);
	}
}
