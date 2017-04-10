package com.ocam.service.impl.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;

@Component
public class FindActivityHikers {

	private ActivityRepository activityRepository;

	@Autowired
	public FindActivityHikers(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = true)
	public List<Hiker> execute(Long activityId) throws BusinessException {

		if (!assertActivityId(activityId)) {
			throw new BusinessException("ID de actividad inválida");
		}
		Activity activity = activityRepository.findOne(activityId);

		if (!assertActivity(activity)) {
			throw new BusinessException(
					"La ID no pertenece a ninguna actividad");
		}

		return new ArrayList<Hiker>(activity.getHikers());
	}

	/**
	 * Comprueba que la actividad exista
	 * 
	 * @param activity
	 * @return
	 */
	private boolean assertActivity(Activity activity) {
		return activity != null && !activity.getDeleted();
	}

	private boolean assertActivityId(Long id) {
		return id != null;
	}
}
