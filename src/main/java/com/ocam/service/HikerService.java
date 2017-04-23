package com.ocam.service;

import java.util.List;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;

public interface HikerService {

	Hiker findHikerByLoginPassword(String login, String password)
			throws BusinessException;

	Hiker findHikerByLogin(String login);

	void updateHiker(Hiker hiker) throws BusinessException;

	void saveHiker(HikerDTO hiker) throws BusinessException;

	void changePassword(HikerDTO hikerDto) throws BusinessException;

	Hiker findHikerByEmail(String email);

	void validateHiker(String code) throws BusinessException;

	List<Activity> findHikerFinishActivities(String login)
			throws BusinessException;
}
