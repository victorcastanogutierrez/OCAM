package com.ocam.service.impl.activity;

import java.io.UnsupportedEncodingException;

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
		try {
			if (!assertTrackSize(activity.getTrack())) {
				throw new BusinessException(
						"Track de la ruta demasiado grande (Máximo 4MB)");
			}
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(
					"Error procesando datos de track de la ruta");
		}
		activity.setOwner(hiker);
		activity.setStatus(ActivityStatus.PENDING);
		this.activityRepository.save(activity);
		hiker.getOwneds().add(activity);
		return activity;
	}

	private boolean assertTrackSize(String track)
			throws UnsupportedEncodingException {
		final byte[] utf8Bytes = track.getBytes("UTF-8");
		System.out.println(utf8Bytes.length);
		return utf8Bytes.length <= 3145728; // 4 MB
	}

	private boolean assertHiker(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertHikerName(String login) {
		return login != null && !login.isEmpty();
	}
}
