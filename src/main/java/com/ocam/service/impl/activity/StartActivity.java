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
public class StartActivity {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	/**
	 * Inicia una actividad. Para ello comprueba que los datos son correctos
	 * (ver métodos), establece la password a la actividad, la cambia de estado
	 * e incluye los guías como hikers
	 * 
	 * @param activityRepository
	 */
	@Autowired
	public StartActivity(ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, String password, String user)
			throws BusinessException {

		if (!assertUserNotNull(user)) {
			throw new BusinessException("Guía de la activida inválido");
		}
		if (assertActivityNotNull(activityId)) {
			throw new BusinessException("Actividad inválida");
		}
		if (assertActivityPassword(password)) {
			throw new BusinessException("Password inválida");
		}

		Activity activity = activityRepository.findOne(activityId);
		assertActivity(activity);
		Hiker hiker = hikerRepository.findByLogin(user);
		assertGuide(activity, hiker);

		includeGuideAsHiker(activity, hiker);
		activity.setPassword(password);
		activity.setStatus(ActivityStatus.RUNNING);
	}

	private void assertGuide(Activity activity, Hiker hiker)
			throws BusinessException {
		if (hiker == null) {
			throw new BusinessException("Guía de la actividad inválido");
		}
		if (!assertHikerGuide(hiker, activity)) {
			throw new BusinessException("No eres guía de la actividad");
		}
	}

	private Boolean assertHikerGuide(Hiker hiker, Activity activity) {
		for (Hiker h : activity.getGuides()) {
			if (h.getId().equals(hiker.getId())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private boolean assertUserNotNull(String user) {
		return user != null;
	}

	/**
	 * Método que incluye al guía como participante de una actividad
	 * 
	 * @param activity
	 */
	private void includeGuideAsHiker(Activity activity, Hiker hiker) {
		hiker.getActivities().add(activity);
		activity.getHikers().add(hiker);
	}

	/**
	 * Comprueba que la actividad exista, no esté borrada y esté en estado
	 * pendiente
	 * 
	 * @param activity
	 * @throws BusinessException
	 */
	private void assertActivity(Activity activity) throws BusinessException {
		if (activity == null || Boolean.TRUE.equals(activity.getDeleted())) {
			throw new BusinessException("Actividad no encontrada");
		} else if (!ActivityStatus.PENDING.equals(activity.getStatus())) {
			throw new BusinessException(
					"La actividad debe estar pendiente de realización para ser iniciada");
		}
	}

	private boolean assertActivityPassword(String password) {
		return password == null || password.isEmpty();
	}

	private boolean assertActivityNotNull(Long activityId) {
		return activityId == null;
	}
}
