package com.ocam.service.impl.activity;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.repository.ActivityRepository;

@Component
public class FindLastActivityReports {

	private ActivityRepository activityRepository;

	@Autowired
	public FindLastActivityReports(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = true)
	public Set<Object[]> execute(Activity activity) {
		return activityRepository.findLastActivityReports(activity);
	}
}
