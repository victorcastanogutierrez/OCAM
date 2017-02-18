package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocam.repository.ActivityRepository;

@Component
public class FindLastActivityReports {

	private ActivityRepository activityRepository;

	@Autowired
	public FindLastActivityReports(ActivityRepository activityRepository)
	{
		this.activityRepository = activityRepository;
	}
	
	public Object execute() {
		//TODO
		return null;
	}
}
