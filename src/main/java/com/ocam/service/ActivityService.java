package com.ocam.service;

import java.util.List;
import java.util.Set;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;

public interface ActivityService {

	Activity saveActivity(ActivityHikerDTO activity) throws BusinessException;

	void updateActivity(Activity activity);

	Set<Report> findLastActivityReports(Long activityId);

	List<Activity> findAllPendingActivities(ActivityDTO criteria)
			throws BusinessException;

	Set<Report> findActivityReportsByHiker(Long activityId, Long hikerId);

	void saveActivityReport(Long activityId, Long hikerId, Report report);

	void JoinActivityHiker(Long activityId, Long hikerId);

	void startActivity(Long activityId);

	void closeActivity(Long activityId);

	void checkActivityPassword(ActivityDTO act) throws BusinessException;

}
