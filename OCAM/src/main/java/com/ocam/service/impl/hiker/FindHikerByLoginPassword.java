package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;

@Component
public class FindHikerByLoginPassword {

	private HikerRepository hikerRepository;

	@Autowired
	public FindHikerByLoginPassword(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Hiker execute(String login, String password) {
		return this.hikerRepository.findByLoginAndPassword(login, password);
	}

}
