package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.repository.ActivityRepository;

@Component
public class SaveActivity {

	private ActivityRepository activityRepository;

	@Autowired
	public SaveActivity(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Activity activity) {
		this.activityRepository.save(activity);
	}
}
