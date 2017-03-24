package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;

@Component
public class FindHikerByEmail {

	private HikerRepository hikerRepository;

	@Autowired
	public FindHikerByEmail(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Hiker execute(String email) {
		if (!assertEmail(email)) {
			return null;
		}
		Hiker h = this.hikerRepository.findByEmail(email);
		return h;
	}

	private boolean assertEmail(String email) {
		return email != null;
	}

}
