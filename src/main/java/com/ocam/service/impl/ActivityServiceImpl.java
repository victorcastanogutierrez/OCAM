package com.ocam.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.impl.activity.FindActivityReportsByHiker;
import com.ocam.service.impl.activity.FindAllPendingActivities;
import com.ocam.service.impl.activity.FindLastActivityReports;
import com.ocam.service.impl.activity.JoinActivityHiker;
import com.ocam.service.impl.activity.SaveActivity;
import com.ocam.service.impl.activity.SaveActivityReport;
import com.ocam.service.impl.activity.UpdateActivity;
import com.ocam.service.impl.activity.UpdateActivityStatus;

@Service
public class ActivityServiceImpl implements ActivityService {

	private FindLastActivityReports findLastActivityReports;
	private SaveActivity saveActivity;
	private UpdateActivity updateActivity;
	private FindAllPendingActivities findAllPendingActivities;
	private FindActivityReportsByHiker findActivityReportsByHiker;
	private SaveActivityReport saveActivityReport;
	private JoinActivityHiker joinActivityHiker;
	private UpdateActivityStatus updateActivityStatus;

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository,
			FindLastActivityReports findLastActivityReports,
			SaveActivity saveActivity, UpdateActivity updateActivity,
			FindAllPendingActivities findAllPendingActivities,
			FindActivityReportsByHiker findActivityReportsByHiker,
			SaveActivityReport saveActivityReport,
			JoinActivityHiker joinActivityHiker,
			UpdateActivityStatus updateActivityStatus) {
		this.findLastActivityReports = findLastActivityReports;
		this.saveActivity = saveActivity;
		this.updateActivity = updateActivity;
		this.findAllPendingActivities = findAllPendingActivities;
		this.findActivityReportsByHiker = findActivityReportsByHiker;
		this.saveActivityReport = saveActivityReport;
		this.joinActivityHiker = joinActivityHiker;
		this.updateActivityStatus = updateActivityStatus;
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
	public List<Activity> findAllPendingActivities(ActivityDTO criteria)
			throws BusinessException {
		return this.findAllPendingActivities.execute(criteria);
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Long activityId,
			Long hikerId) {
		return this.findActivityReportsByHiker.execute(activityId, hikerId);
	}

	@Override
	public void saveActivityReport(Long activityId, Long hikerId,
			Report report) {
		this.saveActivityReport.execute(activityId, hikerId, report);

	}

	@Override
	public void JoinActivityHiker(Long activityId, Long hikerId) {
		this.joinActivityHiker.execute(activityId, hikerId);
	}

	@Override
	public void startActivity(Long activityId) {
		this.updateActivityStatus.execute(activityId, ActivityStatus.RUNNING);

	}

	@Override
	public void closeActivity(Long activityId) {
		this.updateActivityStatus.execute(activityId, ActivityStatus.CLOSED);
	}
}