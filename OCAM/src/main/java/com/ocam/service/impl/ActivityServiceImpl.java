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

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository activityRepository;
	
	private FindLastActivityReports findLastActivityReports; // Este es el command!

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository, FindLastActivityReports findLastActivityReports) {
		this.activityRepository = activityRepository;
		this.findLastActivityReports = findLastActivityReports;
	}

	@Override
	public void saveActivity(Activity activity) {
		this.activityRepository.save(activity);
	}

	@Override
	public void updateActivity(Activity activity) {
		this.activityRepository.save(activity);
	}

	@Override
	public Set<Report> findLastActivityReports() {
		// return (Set<Report>) new findLastActivityReports().execute();
		return (Set<Report>) this.findLastActivityReports.execute();
	}

	@Override
	public Set<Activity> findAllPendingActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Activity activity, Hiker hiker) {
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
