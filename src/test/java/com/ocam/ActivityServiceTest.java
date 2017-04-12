package com.ocam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.ocam.model.ActivityDTO;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.GPSPoint;
import com.ocam.repository.ReportRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.HikerService;
import com.ocam.service.ReportService;

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
	private ReportService reportService;

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
		activity.setTrack("");
		activity.setLongDescription("Descripcion larga");
		activity.setShortDescription("Descripcion corta");
		ActivityHikerDTO act = new ActivityHikerDTO();
		act.setActivity(activity);

		HikerDTO h = new HikerDTO();
		h.setUsername("loginT");
		h.setPassword("passT");
		h.setEmail("emT");

		try {
			hikerService.saveHiker(h);
			Hiker hiker = hikerService.findHikerByLogin(h.getUsername());
			act.setHiker(hiker);
			activityService.saveActivity(act);
			ActivityDTO actDto = new ActivityDTO(Integer.MAX_VALUE, 0);

			List<Activity> pendingActivities;

			pendingActivities = activityService
					.findAllPendingActivities(actDto);

			assertEquals(2, pendingActivities.size());

			activityService.closeActivity(this.act.getId());
			pendingActivities = activityService
					.findAllPendingActivities(actDto);

			assertEquals(1, pendingActivities.size());

			activityService.startActivity(this.act.getId(), "passwd");
			pendingActivities = activityService
					.findAllPendingActivities(actDto);

			assertEquals(2, pendingActivities.size());
		} catch (BusinessException e) {
			assertNull(e);
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindLastActivityReports() {
		ActivityDTO actDto = new ActivityDTO(Integer.MAX_VALUE, 0);
		List<Activity> pendingActivities;
		try {
			pendingActivities = activityService
					.findAllPendingActivities(actDto);
			assertEquals(1, pendingActivities.size());
		} catch (BusinessException e) {
			assertNull(e);
		}

		Set<Report> lastReports = null;
		try {
			lastReports = activityService
					.findLastActivityReports(this.act.getId());
		} catch (BusinessException e) {
			assertNull(e);
		}
		assertEquals(2, lastReports.size());

		Date actual = new Date();
		for (Report r : lastReports) {
			r.setDate(actual);
		}

		try {
			lastReports = activityService
					.findLastActivityReports(this.act.getId());
			for (Report r : lastReports) {
				assertEquals(actual, r.getDate());
			}
		} catch (BusinessException e) {
			assertNull(e);
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
		this.act.setTrack("");
		this.act.setLongDescription("Descripcion larga");
		this.act.setShortDescription("Descripcion corta");

		HikerDTO hdto = new HikerDTO();
		hdto.setUsername("log1");
		hdto.setPassword("passwd1");
		hdto.setEmail("em1");

		HikerDTO hdto2 = new HikerDTO();
		hdto2.setUsername("log2");
		hdto2.setPassword("passwd2");
		hdto2.setEmail("em2");

		Hiker h2 = null;

		ActivityHikerDTO act = new ActivityHikerDTO();
		act.setActivity(this.act);
		act.setHiker(this.hiker);

		try {
			hikerService.saveHiker(hdto);
			this.hiker = hikerService.findHikerByLogin(hdto.getUsername());
			assertNotNull(this.hiker);
			hikerService.saveHiker(hdto2);
			h2 = hikerService.findHikerByLogin(hdto2.getUsername());
			act.setHiker(this.hiker);
			activityService.saveActivity(act);
		} catch (BusinessException e) {
			assertNull(e);
		}

		try {
			activityService.joinActivityHiker(this.act.getId(),
					this.hiker.getLogin());
			activityService.joinActivityHiker(this.act.getId(), h2.getLogin());
		} catch (BusinessException e) {
			assertNotNull(e);
		}

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

		try {
			reportService.save(r1);
			reportService.save(r2);
			reportService.save(r3);
		} catch (BusinessException e) {
			assertNull(e);
		}
	}

}
