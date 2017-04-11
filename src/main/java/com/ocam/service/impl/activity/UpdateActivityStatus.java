package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;

@Component
public class UpdateActivityStatus {

	private ActivityRepository activityRepository;

	@Autowired
	public UpdateActivityStatus(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, ActivityStatus activityStatus)
			throws BusinessException {

		Activity activity = activityRepository.findOne(activityId);

		if (assertActivityNotNull(activity)
				&& assertActivityNotRemoved(activity)) {
			activity.setStatus(activityStatus);
		} else {
			throw new BusinessException("La actividad no existe");
		}
	}

	private boolean assertActivityNotRemoved(Activity activity) {
		return !activity.getDeleted();
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
