package com.ocam.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Override
	public void saveActivity(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateActivity(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findLastActivityReports() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Activity> findAllPendingActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Hiker hiker) {
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
