package com.ocam;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ocam.model.Activity;
import com.ocam.model.GPSPoint;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.repository.ReportRepository;
import com.ocam.service.ActivityService;
import com.ocam.service.HikerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

	final Logger log = Logger.getLogger(ActivityServiceTest.class.getName());

	@Autowired
	private ActivityService activityService;

	@Autowired
	private HikerService hikerService;

	@Autowired
	private ReportRepository reportService;

	private Activity act;

	@Before
	public void setUp() {
		act = new Activity();
		act.setStartDate(new Date());
		act.setTrack("testTrack");
		activityService.saveActivity(act);

		Hiker hiker = new Hiker();
		hiker.setLogin("log");
		hiker.setEmail("em");
		hiker.setPassword("pas");
		hikerService.saveHiker(hiker);

		Hiker hiker2 = new Hiker();
		hiker2.setLogin("log2");
		hiker2.setEmail("em2");
		hiker2.setPassword("pas2");
		hikerService.saveHiker(hiker2);

		Report rep = new Report();
		rep.setHiker(hiker);
		rep.setActivity(act);
		rep.setDate(new Date());
		rep.setPoint(new GPSPoint());
		reportService.save(rep);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -1);
		Report rep2 = new Report();
		rep2.setHiker(hiker);
		rep2.setActivity(act);
		rep2.setDate(c.getTime());
		rep2.setPoint(new GPSPoint());
		reportService.save(rep2);

		Report rep3 = new Report();
		rep3.setHiker(hiker2);
		rep3.setActivity(act);
		rep3.setDate(new Date());
		rep3.setPoint(new GPSPoint());
		reportService.save(rep3);
	}

	@Test
	public void testActivityLastReports() {
		Set<Object[]> reps = this.activityService.findLastActivityReports(act);

	}
}
