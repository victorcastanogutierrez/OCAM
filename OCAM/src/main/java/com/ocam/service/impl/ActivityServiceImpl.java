package com.ocam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.repository.ActivityRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.impl.activity.FindLastActivityReports;
import com.ocam.service.impl.activity.SaveActivity;
import com.ocam.service.impl.activity.UpdateActivity;

@Service
public class ActivityServiceImpl implements ActivityService {

	private FindLastActivityReports findLastActivityReports;
	private SaveActivity saveActivity;
	private UpdateActivity updateActivity;

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository,
			FindLastActivityReports findLastActivityReports,
			SaveActivity saveActivity, UpdateActivity updateActivity) {
		this.findLastActivityReports = findLastActivityReports;
		this.saveActivity = saveActivity;
		this.updateActivity = updateActivity;
	}

	@Override
	public void saveActivity(Activity activity) {
		this.saveActivity.execute(activity);
	}

	@Override
	public void updateActivity(Activity activity) {
		this.updateActivity.execute(activity);
	}

	@Override
	public Set<Object[]> findLastActivityReports(Activity activity) {
		return this.findLastActivityReports.execute(activity);
	}

	@Override
	public Set<Activity> findAllPendingActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Activity activity,
			Hiker hiker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveActivityReport(Activity activity, Report report) {
		// TODO Auto-generated method stub

	}

	@Override
	public void JoinActivityHiker(Activity activity, Hiker hiker) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivity(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeActivity(Activity activity) {
		// TODO Auto-generated method stub

	}

}
