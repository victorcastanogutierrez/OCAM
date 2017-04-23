package com.ocam.service.impl.hiker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;

@Component
public class FindHikerFinishActivities {

	private HikerRepository hikerRepository;

	@Autowired
	public FindHikerFinishActivities(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public List<Activity> execute(String login) throws BusinessException {
		if (!assertEmail(login)) {
			throw new BusinessException("Login inválido");
		}
		Hiker h = this.hikerRepository.findByLogin(login);
		if (!assertHikerNotNull(h)) {
			throw new BusinessException("Hiker no encontrado");
		}

		return new ArrayList<Activity>(
				hikerRepository.findHikerActivities(login));
	}

	private boolean assertHikerNotNull(Hiker h) {
		return h != null;
	}

	private boolean assertEmail(String email) {
		return email != null;
	}

}
