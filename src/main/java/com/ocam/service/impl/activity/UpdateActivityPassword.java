package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;

@Component
public class UpdateActivityPassword {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public UpdateActivityPassword(ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public Activity execute(ActivityHikerDTO activitydto)
			throws BusinessException {

		String hLogin = activitydto.getRequestUser();
		if (!assertHikerName(hLogin)) {
			throw new BusinessException(
					"No tienes permisos para cambiar la password");
		}

		if (!assertActivity(activitydto.getActivity())) {
			throw new BusinessException("Actividad inválida");
		}
		Activity activity = activitydto.getActivity();

		if (!assertPasswordValid(activity)) {
			throw new BusinessException(
					"La password debe ser entre 4 y 12 caracteres");
		}

		if (!assertActivityId(activity)) {
			throw new BusinessException("No se pudo cambiar la password");
		}
		Activity act = activityRepository.findOne(activity.getId());
		if (!assertActivity(act)) {
			throw new BusinessException("No se pudo cambiar la password");
		}

		Hiker hiker = hikerRepository.findByLogin(hLogin);
		if (!assertHiker(hiker)
				|| !assertGuiaActividad(act, activitydto.getRequestUser())) {
			throw new BusinessException(
					"No tienes permisos para cambiar la password");
		}

		act.setPassword(activitydto.getActivity().getPassword());
		return activity;
	}

	private boolean assertPasswordValid(Activity activity) {
		return !(activity.getPassword() == null
				|| activity.getPassword().isEmpty()
				|| activity.getPassword().length() < 4
				|| activity.getPassword().length() > 12);
	}

	private boolean assertActivity(Activity act) {
		return act != null;
	}

	private boolean assertActivityId(Activity activity) {
		return activity.getId() != null;
	}

	/**
	 * Comprueba que el hiker sea guía de la actividad
	 * 
	 * @param activity
	 * @param hiker
	 * @return
	 */
	private Boolean assertGuiaActividad(Activity activity, String hiker) {
		for (Hiker h : activity.getGuides()) {
			if (h.getLogin().equals(hiker)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private boolean assertHiker(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertHikerName(String login) {
		return login != null && !login.isEmpty();
	}
}
