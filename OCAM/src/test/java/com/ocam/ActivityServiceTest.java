package com.ocam;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.types.GPSPoint;
import com.ocam.repository.ReportRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.HikerService;

@Rollback(true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

	final Logger log = Logger.getLogger(HikerServiceTest.class.getName());

	private Activity act;
	private Hiker hiker;
	private Date actualDate = new Date();

	@Autowired
	private ActivityService activityService;

	@Autowired
	private HikerService hikerService;

	@Autowired
	private ReportRepository reportRepository;

	@Before
	public void test() {
		setUpDate();
		testSave();
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindAllPending() {
		Activity activity = new Activity();
		activity.setStartDate(new Date());
		activity.setTrack("TrackTest");
		activityService.saveActivity(activity);

		Set<Activity> pendingActivities = activityService
				.findAllPendingActivities();

		assertEquals(2, pendingActivities.size());

		activityService.closeActivity(this.act.getId());
		pendingActivities = activityService.findAllPendingActivities();

		assertEquals(1, pendingActivities.size());

		activityService.startActivity(this.act.getId());
		pendingActivities = activityService.findAllPendingActivities();

		assertEquals(1, pendingActivities.size());
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindLastActivityReports() {
		Set<Activity> pendingActivities = activityService
				.findAllPendingActivities();
		assertEquals(1, pendingActivities.size());

		Set<Report> lastReports = activityService
				.findLastActivityReports(this.act.getId());
		assertEquals(2, lastReports.size());

		Date actual = new Date();
		for (Report r : lastReports) {
			r.setDate(actual);
		}

		lastReports = activityService.findLastActivityReports(this.act.getId());
		for (Report r : lastReports) {
			assertEquals(actual, r.getDate());
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindActivityReportsByHiker() {
		assertEquals(2,
				activityService.findActivityReportsByHiker(this.act.getId(),
						this.hiker.getId()).size());

		Report r = getNewReport();
		this.reportRepository.save(r);

		Set<Report> reports = activityService.findActivityReportsByHiker(
				this.act.getId(), this.hiker.getId());
		assertEquals(3, reports.size());

		this.reportRepository.delete(reports.stream().findFirst().get());
		assertEquals(2,
				activityService.findActivityReportsByHiker(this.act.getId(),
						this.hiker.getId()).size());

		reports.forEach(x -> this.reportRepository.delete(x));
		assertEquals(0,
				activityService.findActivityReportsByHiker(this.act.getId(),
						this.hiker.getId()).size());
	}

	private Report getNewReport() {
		Report r = new Report();
		r.setDate(this.actualDate);
		r.setActivity(act);
		r.setHiker(hiker);
		r.setPoint(new GPSPoint());
		return r;
	}

	private void setUpDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.actualDate);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		this.actualDate = calendar.getTime();
	}

	@Transactional(readOnly = false)
	private void testSave() {
		this.act = new Activity();
		this.act.setStartDate(new Date());
		this.act.setTrack("track");

		activityService.saveActivity(this.act);

		this.hiker = new Hiker();
		this.hiker.setLogin("log1");
		this.hiker.setPassword("passwd1");
		this.hiker.setEmail("em1");

		Hiker h2 = new Hiker();
		h2.setLogin("log2");
		h2.setPassword("passwd2");
		h2.setEmail("em2");

		hikerService.saveHiker(hiker);
		hikerService.saveHiker(h2);

		activityService.JoinActivityHiker(this.act.getId(), this.hiker.getId());
		activityService.JoinActivityHiker(this.act.getId(), h2.getId());

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date date2 = c.getTime();
		GPSPoint gps = new GPSPoint();

		Report r1 = new Report();
		r1.setHiker(this.hiker);
		r1.setDate(this.actualDate);
		r1.setPoint(gps);

		Report r2 = new Report();
		r2.setHiker(this.hiker);
		r2.setDate(date2);
		r2.setPoint(gps);

		Report r3 = new Report();
		r3.setHiker(h2);
		r3.setDate(this.actualDate);
		r3.setPoint(gps);

		activityService.saveActivityReport(this.act.getId(), this.hiker.getId(),
				r1);
		activityService.saveActivityReport(this.act.getId(), this.hiker.getId(),
				r2);
		activityService.saveActivityReport(this.act.getId(), h2.getId(), r3);
	}

}
