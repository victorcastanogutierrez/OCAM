package com.ocam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.model.types.GPSPoint;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.ReportService;

@Rollback(true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTest {

	private int hikers = 0;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private HikerRepository hikerRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testSaveHikerAndFindByProperties() {
		Hiker h = getHikerMock();
		Activity a = getActivityMock();
		activityRepository.save(a);

		Report r = getReportMock(a, h);

		try {
			reportService.save(r);
			fail("Debería haber error por hiker asociado no existente");
		} catch (BusinessException e) {
		}

		hikerRepository.save(h);
		try {
			reportService.save(r);
			fail("Debería haber error por hiker no participante");
		} catch (BusinessException e) {
		}

		a.getHikers().add(h);
		h.getActivities().add(a);
		a.setStatus(ActivityStatus.RUNNING);

		assertTrue(activityService
				.findActivityReportsByHiker(a.getId(), h.getEmail()).isEmpty());
		try {
			reportService.save(r);
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}

		assertEquals(1, activityService
				.findActivityReportsByHiker(a.getId(), h.getEmail()).size());
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void findLastHikerACtivityReportAndAllByActivity() {
		try {
			reportService.findHikerLastActivityReport(10L, "login");
			fail("Debería haber error por hiker asociado no existente");
		} catch (BusinessException e) {
		}

		Activity a = getActivityMock();
		activityRepository.save(a);
		Hiker h = getHikerMock();
		hikerRepository.save(h);
		Report r = getReportMock(a, h);

		try {
			assertTrue(reportService
					.findHikerLastActivityReport(a.getId(), h.getLogin())
					.isEmpty());

			a.getReports().add(r);
			h.getReports().add(r);
			reportRepository.save(r);
			assertFalse(reportService
					.findHikerLastActivityReport(a.getId(), h.getLogin())
					.isEmpty());

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, 20);
			Report r2 = getReportMock(a, h);
			r2.setDate(c.getTime());
			a.getReports().add(r);
			h.getReports().add(r2);
			a.getReports().add(r2);
			reportRepository.save(r2);
			Set<Report> set = reportService
					.findHikerLastActivityReport(a.getId(), h.getLogin());
			Iterator<Report> iter = set.iterator();
			assertEquals(c.getTime(), iter.next().getDate());
			assertEquals(2, reportService.findAllByActivity(h.getId()).size());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	/**
	 * Retorna un hiker diferente en cada llamada
	 * 
	 * @return
	 */
	private Hiker getHikerMock() {
		Hiker hiker = new Hiker();
		hiker.setLogin("login" + hikers);
		hiker.setEmail("email" + hikers);
		hiker.setPassword("password");
		this.hikers++;
		return hiker;
	}

	private Activity getActivityMock() {
		Activity activity = new Activity();
		activity.setShortDescription("Descrt");
		activity.setTrack("track");
		activity.setStartDate(new Date());
		activity.setStatus(ActivityStatus.PENDING);
		return activity;
	}

	private Report getReportMock(Activity act, Hiker hik) {
		Report r = new Report();
		r.setActivity(act);
		r.setHiker(hik);
		r.setDate(new Date());
		GPSPoint gps = new GPSPoint();
		gps.setLatitude(10);
		gps.setLongitude(10000);
		r.setPoint(gps);
		return r;
	}
}
