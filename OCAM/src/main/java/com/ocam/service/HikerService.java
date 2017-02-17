package com.ocam.service;

import com.ocam.model.Hiker;

public interface HikerService {

	Hiker findHikerByLoginPassword(String login, String password);
	
	void updateHiker(Hiker hiker);
	
	void saveHiker(Hiker hiker);
}
