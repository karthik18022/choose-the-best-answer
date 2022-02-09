package com.answer.best.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessDenied implements AccessDeniedHandler {

	private static final long serialVersionUID = -7858869558953243875L;

	
//	public void handle(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//
//		    response.setContentType("application/json");
//	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	        response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
//	}


	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + accessDeniedException.getMessage() + "\" }");
        response.getOutputStream().println(response.getStatus());
	}


//	@Override
//	public void handle(HttpServletRequest request, HttpServletResponse response,
//			AccessDeniedException accessDeniedException) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		  response.setContentType("application/json");
//	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	        response.getOutputStream().println("{ \"error\": \"" + "access denied" + "\" }");
//	}


	
}