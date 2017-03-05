package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;

@Component
public class JoinActivityHiker {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public JoinActivityHiker(ReportRepository reportRepository,
			ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, Long hikerId) {

		Activity activity = activityRepository.findOne(activityId);
		Hiker hiker = hikerRepository.findOne(hikerId);

		if (assertActivityNotNull(activity) && assertHikerNotNull(hiker)) {
			activity.getHikers().add(hiker);
			hiker.getActivities().add(activity);
		}
	}

	private Boolean assertHikerNotNull(Hiker hiker) {
		return hiker != null;
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
