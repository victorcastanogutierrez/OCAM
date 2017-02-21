package com.ocam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.repository.ActivityRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.impl.activity.FindActivityReportsByHiker;
import com.ocam.service.impl.activity.FindAllPendingActivities;
import com.ocam.service.impl.activity.FindLastActivityReports;
import com.ocam.service.impl.activity.SaveActivity;
import com.ocam.service.impl.activity.SaveActivityReport;
import com.ocam.service.impl.activity.UpdateActivity;

@Service
public class ActivityServiceImpl implements ActivityService {

	private FindLastActivityReports findLastActivityReports;
	private SaveActivity saveActivity;
	private UpdateActivity updateActivity;
	private FindAllPendingActivities findAllPendingActivities;
	private FindActivityReportsByHiker findActivityReportsByHiker;
	private SaveActivityReport saveActivityReport;

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository,
			FindLastActivityReports findLastActivityReports,
			SaveActivity saveActivity, UpdateActivity updateActivity,
			FindAllPendingActivities findAllPendingActivities,
			FindActivityReportsByHiker findActivityReportsByHiker,
			SaveActivityReport saveActivityReport) {
		this.findLastActivityReports = findLastActivityReports;
		this.saveActivity = saveActivity;
		this.updateActivity = updateActivity;
		this.findAllPendingActivities = findAllPendingActivities;
		this.findActivityReportsByHiker = findActivityReportsByHiker;
		this.saveActivityReport = saveActivityReport;
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
	public Set<Report> findLastActivityReports(Long activityId) {
		return this.findLastActivityReports.execute(activityId);
	}

	@Override
	public Set<Activity> findAllPendingActivities() {
		return this.findAllPendingActivities.execute();
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Long activityId,
			Long hikerId) {
		return this.findActivityReportsByHiker.execute(activityId, hikerId);
	}

	@Override
	public void saveActivityReport(Long activityId, Report report) {
		this.saveActivityReport.execute(activityId, report);

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
