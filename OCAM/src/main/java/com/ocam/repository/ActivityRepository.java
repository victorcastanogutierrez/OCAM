package com.ocam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	
	

}
