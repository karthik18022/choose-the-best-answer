package com.answer.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.answer.best.entity.Test;

@Repository
public interface TestRepo extends JpaRepository<Test,Integer>{
	
	

}
