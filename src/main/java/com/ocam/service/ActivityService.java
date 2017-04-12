package com.ocam.service;

import java.util.List;
import java.util.Set;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;

public interface ActivityService {

	Activity saveActivity(ActivityHikerDTO activity) throws BusinessException;

	void updateActivity(Activity activity);

	Set<Report> findLastActivityReports(Long activityId)
			throws BusinessException;

	List<Activity> findAllPendingActivities(ActivityDTO criteria)
			throws BusinessException;

	Set<Report> findActivityReportsByHiker(Long activityId, Long hikerId);

	void joinActivityHiker(Long activityId, String login)
			throws BusinessException;

	void startActivity(Long activityId, String password)
			throws BusinessException;

	void closeActivity(Long activityId) throws BusinessException;

	void checkActivityPassword(ActivityDTO act) throws BusinessException;

	Activity findActivityById(Long id);

	void updateActivityPassword(ActivityHikerDTO act) throws BusinessException;

	List<Hiker> findActivityHikers(Long activityId) throws BusinessException;

}
