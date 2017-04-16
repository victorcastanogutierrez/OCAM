package com.ocam.service.impl.activity;

import java.util.Iterator;

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
public class DeleteActivityHiker {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public DeleteActivityHiker(ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, String login)
			throws BusinessException {

		if (!assertActivityIdHikerLogin(activityId, login)) {
			throw new BusinessException("Actividad y usuario requeridos.");
		}

		Activity activity = activityRepository.findOne(activityId);
		if (!assertActivityNotNull(activity)) {
			throw new BusinessException("Actividad no encontrada.");
		}

		if (!assertActivityRunning(activity)) {
			throw new BusinessException(
					"No se puede eliminar un hiker de una actividad si no está en curso.");
		}

		Hiker hiker = hikerRepository.findByLogin(login);
		if (!assertHikerNotNull(hiker)) {
			throw new BusinessException("Hiker no encontrado.");
		}

		Boolean removed = removeActivityHiker(login, activity);

		if (Boolean.FALSE.equals(removed)) {
			throw new BusinessException(
					"El hiker no pertenecía a la actividad.");
		} else {
			removeHikerActivity(activity, hiker);
		}
	}

	/**
	 * Elimina de la actividad el hiker
	 * 
	 * @param activity
	 * @param hiker
	 */
	private void removeHikerActivity(Activity activity, Hiker hiker) {
		for (Iterator<Activity> iterator = hiker.getActivities()
				.iterator(); iterator.hasNext();) {
			Activity act = iterator.next();
			if (act.getId().equals(activity.getId())) {
				iterator.remove();
				break;
			}
		}
	}

	/**
	 * Elimina la asociación desde el Hiker a la actividad
	 * 
	 * @param login
	 * @param activity
	 * @return
	 */
	private Boolean removeActivityHiker(String login, Activity activity) {
		Boolean removed = Boolean.FALSE;
		for (Iterator<Hiker> iterator = activity.getHikers()
				.iterator(); iterator.hasNext();) {
			Hiker h = iterator.next();
			if (h.getLogin().equals(login)) {
				iterator.remove();
				removed = Boolean.TRUE;
				break;
			}
		}
		return removed;
	}

	private boolean assertHikerNotNull(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertActivityRunning(Activity activity) {
		return ActivityStatus.RUNNING.equals(activity.getStatus());
	}

	private boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}

	private boolean assertActivityIdHikerLogin(Long activityId, String login) {
		return activityId != null && login != null;
	}

}
