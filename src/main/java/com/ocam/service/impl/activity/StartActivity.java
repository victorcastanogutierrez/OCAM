package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;

@Component
public class StartActivity {

	private ActivityRepository activityRepository;

	/**
	 * Inicia una actividad. Para ello comprueba que los datos son correctos
	 * (ver métodos), establece la password a la actividad, la cambia de estado
	 * e incluye los guías como hikers
	 * 
	 * @param activityRepository
	 */
	@Autowired
	public StartActivity(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, String password)
			throws BusinessException {

		if (assertActivityNotNull(activityId)) {
			throw new BusinessException("Actividad inválida");
		}
		if (assertActivityPassword(password)) {
			throw new BusinessException("Password inválida");
		}

		Activity activity = activityRepository.findOne(activityId);
		assertActivity(activity);

		includeGuidesAsHikers(activity);
		activity.setPassword(password);
		activity.setStatus(ActivityStatus.RUNNING);

	}

	/**
	 * Método que incluye los guías como hikers de la actividad
	 * 
	 * @param activity
	 */
	private void includeGuidesAsHikers(Activity activity) {
		for (Hiker h : activity.getGuides()) {
			h.getActivities().add(activity);
			activity.getHikers().add(h);
		}
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
