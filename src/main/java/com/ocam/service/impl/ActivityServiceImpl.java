package com.ocam.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.impl.activity.CheckActivityPassword;
import com.ocam.service.impl.activity.DeleteActivityHiker;
import com.ocam.service.impl.activity.FindActivityById;
import com.ocam.service.impl.activity.FindActivityHikers;
import com.ocam.service.impl.activity.FindActivityReportsByHiker;
import com.ocam.service.impl.activity.FindAllPendingActivities;
import com.ocam.service.impl.activity.FindLastActivityReports;
import com.ocam.service.impl.activity.JoinActivityHiker;
import com.ocam.service.impl.activity.SaveActivity;
import com.ocam.service.impl.activity.StartActivity;
import com.ocam.service.impl.activity.UpdateActivity;
import com.ocam.service.impl.activity.UpdateActivityPassword;
import com.ocam.service.impl.activity.UpdateActivityStatus;

@Service
public class ActivityServiceImpl implements ActivityService {

	private FindLastActivityReports findLastActivityReports;
	private SaveActivity saveActivity;
	private UpdateActivity updateActivity;
	private FindAllPendingActivities findAllPendingActivities;
	private FindActivityReportsByHiker findActivityReportsByHiker;
	private JoinActivityHiker joinActivityHiker;
	private UpdateActivityStatus updateActivityStatus;
	private CheckActivityPassword checkActivityPassword;
	private FindActivityById findActivityById;
	private StartActivity startActivity;
	private UpdateActivityPassword updateActivityPassword;
	private FindActivityHikers findActivityHikers;
	private DeleteActivityHiker deleteActivityHiker;

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository,
			FindLastActivityReports findLastActivityReports,
			SaveActivity saveActivity, UpdateActivity updateActivity,
			FindAllPendingActivities findAllPendingActivities,
			FindActivityReportsByHiker findActivityReportsByHiker,
			JoinActivityHiker joinActivityHiker,
			UpdateActivityStatus updateActivityStatus,
			CheckActivityPassword checkActivityPassword,
			FindActivityById findActivityById, StartActivity startActivity,
			UpdateActivityPassword updateActivityPassword,
			FindActivityHikers findActivityHikers,
			DeleteActivityHiker deleteActivityHiker) {
		this.findLastActivityReports = findLastActivityReports;
		this.saveActivity = saveActivity;
		this.updateActivity = updateActivity;
		this.findAllPendingActivities = findAllPendingActivities;
		this.findActivityReportsByHiker = findActivityReportsByHiker;
		this.joinActivityHiker = joinActivityHiker;
		this.updateActivityStatus = updateActivityStatus;
		this.checkActivityPassword = checkActivityPassword;
		this.findActivityById = findActivityById;
		this.startActivity = startActivity;
		this.updateActivityPassword = updateActivityPassword;
		this.findActivityHikers = findActivityHikers;
		this.deleteActivityHiker = deleteActivityHiker;
	}

	@Override
	public Activity saveActivity(ActivityHikerDTO activity)
			throws BusinessException {
		return this.saveActivity.execute(activity);
	}

	@Override
	public void updateActivity(Activity activity) {
		this.updateActivity.execute(activity);
	}

	@Override
	public Set<Report> findLastActivityReports(Long activityId)
			throws BusinessException {
		return this.findLastActivityReports.execute(activityId);
	}

	@Override
	public List<Activity> findAllPendingActivities(ActivityDTO criteria)
			throws BusinessException {
		return this.findAllPendingActivities.execute(criteria);
	}

	@Override
	public Set<Report> findActivityReportsByHiker(Long activityId,
			String hikerEmail) {
		return this.findActivityReportsByHiker.execute(activityId, hikerEmail);
	}

	@Override
	public void joinActivityHiker(Long activityId, String login,
			String password) throws BusinessException {
		this.joinActivityHiker.execute(activityId, login, password);
	}

	@Override
	public void startActivity(Long activityId, String password, String user)
			throws BusinessException {
		this.startActivity.execute(activityId, password, user);
	}

	@Override
	public void closeActivity(Long activityId) throws BusinessException {
		this.updateActivityStatus.execute(activityId, ActivityStatus.CLOSED);
	}

	@Override
	public void checkActivityPassword(ActivityDTO act)
			throws BusinessException {
		this.checkActivityPassword.execute(act);
	}

	@Override
	public Activity findActivityById(Long id) {
		return this.findActivityById.execute(id);
	}

	@Override
	public void updateActivityPassword(ActivityHikerDTO act)
			throws BusinessException {
		this.updateActivityPassword.execute(act);

	}

	@Override
	public List<Hiker> findActivityHikers(Long activityId)
			throws BusinessException {
		return this.findActivityHikers.execute(activityId);
	}

	@Override
	public void deleteActivityHiker(Long activityId, String hikerLogin)
			throws BusinessException {
		this.deleteActivityHiker.execute(activityId, hikerLogin);

	}
}
