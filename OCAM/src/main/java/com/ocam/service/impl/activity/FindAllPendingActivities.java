package com.ocam.service.impl.activity;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;

@Component
public class FindAllPendingActivities {

	private ActivityRepository activityRepository;

	@Autowired
	public FindAllPendingActivities(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = true)
	public Set<Activity> execute() {
		return activityRepository.findAllByStatus(ActivityStatus.PENDING);
	}
}
