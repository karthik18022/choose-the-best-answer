package com.answer.best.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestVO {

	public String email;
	public List<QuestionRequest> request;
}
