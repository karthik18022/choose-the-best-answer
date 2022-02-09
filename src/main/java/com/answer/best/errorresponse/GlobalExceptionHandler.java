package com.answer.best.errorresponse;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.answer.best.message.MessageStore;
import com.answer.best.response.ResponseVo;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
//	@ExceptionHandler()
//	public ResponseEntity<?> specficException(Exception exception,WebRequest request){
//		ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),request.getDescription(false));
//		return new ResponseEntity(errorDetails,HttpStatus.BAD_REQUEST);
//	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> userNameException(Exception exception,WebRequest request){
		ResponseVo responseVo=new ResponseVo();
//		ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),request.getDescription(false));
		responseVo.setCode(HttpServletResponse.SC_BAD_REQUEST);
		responseVo.setStatus(MessageStore.FAILURE);
		responseVo.setMessage(MessageStore.EMAIL_FOUND_EXCEPTION);
		return new ResponseEntity(responseVo,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MySQLIntegrityConstraintViolationException.class)
	public  ResponseEntity<?> validException(Exception exception,WebRequest request){
		ResponseVo responseVo=new ResponseVo();
		responseVo.setCode(HttpServletResponse.SC_EXPECTATION_FAILED);
		responseVo.setStatus(MessageStore.FAILURE);
		responseVo.setMessage(MessageStore.EMAIL_FOUND_EXCEPTION);		
		return new ResponseEntity(responseVo,HttpStatus.BAD_REQUEST);
	}
	
}
