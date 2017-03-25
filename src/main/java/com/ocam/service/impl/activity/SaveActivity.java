package com.ocam.service.impl.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;

@Component
public class SaveActivity {

	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public SaveActivity(ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public Activity execute(ActivityHikerDTO activitydto)
			throws BusinessException {

		Activity activity = activitydto.getActivity();
		String hLogin = activitydto.getHiker().getLogin();
		if (!assertHikerName(hLogin)) {
			throw new BusinessException(
					"Error asignando el propietario de la actividad");
		}

		Hiker hiker = hikerRepository.findByLogin(hLogin);
		if (!assertHiker(hiker)) {
			throw new BusinessException(
					"Error asignando el propietario de la actividad");
		}

		if (!assertTrackSize(activity.getTrack())) {
			throw new BusinessException(
					"Track de la ruta demasiado grande (Máximo 4MB)");
		}

		if (isEditing(activity)
				&& !assertPermissions(activity, activitydto.getRequestUser())) {
			throw new BusinessException(
					"Error de permisos de usuario guardando la actividad");
		}

		if (!assertGuiasActividad(activity)) {
			throw new BusinessException("Error procesando datos de los guías");
		}

		activity.setOwner(hiker);
		activity.setStatus(ActivityStatus.PENDING);
		this.activityRepository.save(activity);
		hiker.getOwneds().add(activity);
		return activity;
	}

	/**
	 * Método que comprueba que el dueño de la actividad es el usuario con login
	 * pasado por parámetro
	 * 
	 * @param activity
	 * @param requestUser
	 * @return
	 */
	private boolean assertPermissions(Activity activity, String requestUser) {
		Hiker h = hikerRepository.findByLogin(requestUser);
		Activity act = activityRepository.findOne(activity.getId());
		if (!assertHiker(h)) {
			return Boolean.FALSE;
		}
		return act.getOwner().getLogin().equals(requestUser);
	}

	/**
	 * Comprueba si se está editando la actividad o guardando en caso contrario
	 * 
	 * @param activity
	 * @return
	 */
	private boolean isEditing(Activity activity) {
		return activity.getId() != null;
	}

	/**
	 * Comprueba que todos los guías adheridos a la actividad existan como
	 * usuarios registrados y no haya ninguno repetido
	 * 
	 * @param activity
	 * @throws BusinessException
	 */
	private Boolean assertGuiasActividad(Activity activity) {
		Set<Hiker> guides = new HashSet<Hiker>(activity.getGuides());
		activity.getGuides().clear();
		for (Hiker h : guides) {
			Hiker guide = hikerRepository.findByEmail(h.getEmail());
			if (assertGuide(guide) && !activity.getGuides().contains(guide)) {
				activity.getGuides().add(guide);
				guide.getActivityGuide().add(activity);
			} else {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	private boolean assertGuide(Hiker guide) {
		return guide != null;
	}

	/**
	 * Método que comprueba que el track de la ruta no pase de 4Mb
	 * 
	 * @param track
	 * @return
	 * @throws BusinessException
	 */
	private boolean assertTrackSize(String track) throws BusinessException {
		byte[] utf8Bytes;
		try {
			utf8Bytes = track.getBytes("UTF-8");
			System.out.println(utf8Bytes.length);
			return utf8Bytes.length <= 3145728; // 4 MB
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(
					"Error procesando datos de track de la ruta");
		}
	}

	private boolean assertHiker(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertHikerName(String login) {
		return login != null && !login.isEmpty();
	}
}
