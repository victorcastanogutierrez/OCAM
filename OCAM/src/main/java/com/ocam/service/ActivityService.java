package com.ocam.service;

import java.util.Set;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;

public interface ActivityService {

	void saveActivity(Activity activity);

	void updateActivity(Activity activity);

	Set<Object[]> findLastActivityReports(Activity activity);

	Set<Activity> findAllPendingActivities();

	Set<Report> findActivityReportsByHiker(Activity activity, Hiker hiker);

	void saveActivityReport(Activity activity, Report report);

	void JoinActivityHiker(Activity activity, Hiker hiker);

	void startActivity(Activity activity);

	void closeActivity(Activity activity);

}
