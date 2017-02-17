package com.ocam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;

@Service
public class HikerServiceImpl implements HikerService {
	
	private HikerRepository hikerRepository;
	
	@Autowired
	public HikerServiceImpl(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Hiker findHikerByLoginPassword(String login, String password) {
		return hikerRepository.findByLoginAndPassword(login, password);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateHiker(Hiker hiker) {
		hikerRepository.save(hiker);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveHiker(Hiker hiker) {
		hikerRepository.save(hiker);		
	}

	
}
