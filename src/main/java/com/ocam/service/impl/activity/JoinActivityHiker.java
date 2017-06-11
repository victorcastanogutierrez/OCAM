package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;

@Component
public class JoinActivityHiker {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public JoinActivityHiker(ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, String login, String password)
			throws BusinessException {

		if (!assertInputParameters(activityId, login, password)) {
			throw new BusinessException("Error uniendose a la actividad");
		}

		Activity activity = activityRepository.findOne(activityId);
		Hiker hiker = hikerRepository.findByLogin(login);

		if (!assertActivityNotNull(activity) || !assertHikerNotNull(hiker)) {
			throw new BusinessException("Error uniendose a la actividad");
		}

		if (!assertActivityPassword(activity, password)) {
			throw new BusinessException("Password de actividad incorrecta.");
		}

		if (!assertHikerJoined(activity, hiker)) {
			throw new BusinessException(
					"El Hiker ya se encuentra como participante");
		}

		if (!assertUniqueActivity(hiker)) {
			throw new BusinessException(
					"Ya estás unido en una actividad que está en curso.");
		}

		activity.getHikers().add(hiker);
		hiker.getActivities().add(activity);
	}

	private boolean assertActivityPassword(Activity activity, String password) {
		return activity.getPassword() != null
				&& activity.getPassword().equals(password);
	}

	/**
	 * Comprueba que no esté unido en una actividad que esté en curso.
	 * 
	 * @param hiker
	 * @return
	 */
	private Boolean assertUniqueActivity(Hiker hiker) {
		for (Activity act : hiker.getActivities()) {
			if (ActivityStatus.RUNNING.equals(act.getStatus())) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	private Boolean assertHikerJoined(Activity activity, Hiker hiker) {
		for (Hiker h : activity.getHikers()) {
			if (h.getId().equals(hiker.getId())) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	private boolean assertInputParameters(Long activityId, String login,
			String password) {
		return activityId != null && login != null && password != null;
	}

	private Boolean assertHikerNotNull(Hiker hiker) {
		return hiker != null;
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
