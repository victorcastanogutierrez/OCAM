package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.repository.ActivityRepository;

@Component
public class FindActivityById {

	private ActivityRepository activityRepository;

	@Autowired
	public FindActivityById(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = true)
	public Activity execute(Long id) {

		if (!assertActivityId(id)) {
			return null;
		}
		return activityRepository.findOne(id);
	}

	private boolean assertActivityId(Long id) {
		return id != null;
	}
}
