package com.answer.best.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.answer.best.entity.Questions;
import com.answer.best.repository.QuestionRepo;
import com.answer.best.response.QuestionResponse;

@Service
public class QuestionImpl {

	@Autowired
	QuestionRepo questionRepo;

	public Questions addQuestion(Questions questions) {
		Questions questionObj = new Questions();
		questionObj.setQuestion(questions.getQuestion());
		questionObj.setOptionA(questions.getOptionA());
		questionObj.setOptionB(questions.getOptionB());
		questionObj.setOptionC(questions.getOptionC());
		questionObj.setOptionD(questions.getOptionD());
		questionObj.setAnswer(questions.getAnswer());
		questionRepo.save(questionObj);
		return questionObj;
	}

	public List<QuestionResponse> getQuestions() {
		List<Questions> questionList = questionRepo.getAllQuestions();
		List<QuestionResponse> finalList = new ArrayList<>();
		if (questionList != null) {
			for (int i = 0; i < questionList.size(); i++) {
				QuestionResponse response = new QuestionResponse();
				response.setQuestionId(questionList.get(i).getQuestionId());
				response.setQuestion(questionList.get(i).getQuestion());
				response.setOptionA(questionList.get(i).getOptionA());
				response.setOptionB(questionList.get(i).getOptionB());
				response.setOptionC(questionList.get(i).getOptionC());
				response.setOptionD(questionList.get(i).getOptionD());
				finalList.add(response);
			}
		}
		return finalList;
	}
}
