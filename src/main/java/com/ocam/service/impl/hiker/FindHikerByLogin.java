package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;

@Component
public class FindHikerByLogin {

	private HikerRepository hikerRepository;

	@Autowired
	public FindHikerByLogin(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Hiker execute(String login) {
		return this.hikerRepository.findByLogin(login);
	}

}
