package com.answer.best.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.answer.best.entity.Questions;
import com.answer.best.exception.ValidatationExcption;
import com.answer.best.repository.QuestionRepo;
import com.answer.best.response.QuestionResponse;
import com.answer.best.store.MessageStore;

@Service
public class QuestionImpl {

	@Autowired
	QuestionRepo questionRepo;

	public Questions addQuestion(Questions questions) {
		Questions questionObj = new Questions();
		if (questions != null) {
			questionObj.setQuestion(questions.getQuestion());
			questionObj.setOptionA(questions.getOptionA());
			questionObj.setOptionB(questions.getOptionB());
			questionObj.setOptionC(questions.getOptionC());
			questionObj.setOptionD(questions.getOptionD());
			questionObj.setAnswer(questions.getAnswer());
			questionRepo.save(questionObj);
		}
		return questionObj;
	}

	public List<QuestionResponse> getQuestions() {
		List<Questions> questionList = questionRepo.getAllQuestions();
		List<QuestionResponse> finalList = new ArrayList<>();
		if (questionList != null) {
			for (Questions response : questionList) {
				QuestionResponse questionResponse = new QuestionResponse();
				questionResponse.setQuestionId(response.getQuestionId());
				questionResponse.setQuestion(response.getQuestion());
				questionResponse.setOptionA(response.getOptionA());
				questionResponse.setOptionB(response.getOptionB());
				questionResponse.setOptionC(response.getOptionC());
				questionResponse.setOptionD(response.getOptionD());
				finalList.add(questionResponse);
			}
		}
		return finalList;
	}
}
