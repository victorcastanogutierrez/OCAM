package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;

/**
 * Clase que encapsula la funcionalidad de buscar una actividad por su id y
 * password
 * 
 * @author Victor
 *
 */
@Component
public class CheckActivityPassword {

	private ActivityRepository activityRepository;

	@Autowired
	public CheckActivityPassword(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	/**
	 * Comprueba que una actividad tenga ya password asociada y que la que envía
	 * el usuario coincide con ella
	 * 
	 * @param criteria
	 * @throws BusinessException
	 *             falla alguna de las validaciones.
	 */
	@Transactional(readOnly = true)
	public void execute(ActivityDTO criteria) throws BusinessException {

		Activity activity = activityRepository.findOne(criteria.getId());

		if (!assertActivity(activity)) {
			throw new BusinessException(
					"Error encontrando la actividad para comprobar la password.");
		}

		if (!assertPassword(criteria)) {
			throw new BusinessException(
					"Error validando la password introducida por el usuario.");
		}

		if (!assertActivityPassword(activity)) {
			throw new BusinessException(
					"La actividad aún no tiene password asociada.");
		}

		if (!criteria.getPassword().equals(activity.getPassword())) {
			throw new BusinessException("Password de actividad incorrecta.");
		}
	}

	private boolean assertActivityPassword(Activity activity) {
		return activity.getPassword() != null;
	}

	private boolean assertPassword(ActivityDTO criteria) {
		return criteria.getPassword() != null;
	}

	private boolean assertActivity(Activity activity) {
		return activity != null;
	}

}
