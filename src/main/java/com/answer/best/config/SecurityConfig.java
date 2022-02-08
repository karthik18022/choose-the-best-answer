package com.answer.best.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.answer.best.dao.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
//	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	JwtAuthenticationEntryPoint unauthorizedHandler;
	
	   @Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        super.configure(auth);
	        auth.userDetailsService(userService);
	    }
	   @Override
	 public void configure(HttpSecurity security) throws Exception {
	        security.csrf().disable()
	                .authorizeRequests()
	                .antMatchers( "/save","/add/question","/authenticate","qestionsV1").permitAll()
	                .anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
	               .and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					;
	        security.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	    }
	   
	 @Bean
	    public PasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
	 
	 @Bean
	   @Override
	    protected AuthenticationManager authenticationManager() throws Exception {
	        return super.authenticationManager();
	    }

}
