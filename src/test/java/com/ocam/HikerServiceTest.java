package com.ocam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.model.types.GPSPoint;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;
import com.ocam.service.HikerService;

@Rollback(true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class HikerServiceTest {

	private int hikers = 0;

	@Autowired
	private HikerService hikerService;

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
		HikerDTO hdto = new HikerDTO();
		hdto.setUsername("login");
		hdto.setPassword("password");
		hdto.setEmail("email");

		Hiker nhiker = null;
		try {
			hikerService.saveHiker(hdto);
			fail("Debe haber error al estar en perfil de testing y por tanto, "
					+ "no poder enviar el email de confirmación de cuenta");
		} catch (BusinessException e) {
		}

		try {
			nhiker = hikerService.findHikerByEmail(hdto.getEmail());
			assertNotNull(nhiker);
			assertEquals("login", nhiker.getLogin());
			assertEquals("email", nhiker.getEmail());

			nhiker = hikerService.findHikerByLoginPassword(hdto.getUsername(),
					"password");
			assertNotNull(nhiker);
			assertEquals("login", nhiker.getLogin());
			assertEquals("email", nhiker.getEmail());

			nhiker = hikerService.findHikerByLogin(hdto.getUsername());
			assertNotNull(nhiker);
			assertEquals("login", nhiker.getLogin());
			assertEquals("email", nhiker.getEmail());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testValidateHiker() {

		try {
			hikerService.validateHiker(null);
			fail("Debería haber error por parámetro inválido");
		} catch (BusinessException e) {
		}

		Hiker h = getHikerMock();
		h.setActiveCode("222");
		hikerRepository.save(h);

		try {
			hikerService.validateHiker("123");
			fail("Debería haber error por código no asociado a hiker");
		} catch (BusinessException e) {
		}

		h.setActive(Boolean.TRUE);
		try {
			hikerService.validateHiker("222");
			fail("Debería haber error por hiker ya activo");
		} catch (BusinessException e) {
		}

		h.setActive(Boolean.FALSE);
		try {
			hikerService.validateHiker("222");
			assertTrue(hikerService.findHikerByEmail(h.getEmail()).getActive());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void findHikerFinishActivities() {
		Hiker h = getHikerMock();
		hikerRepository.save(h);
		Activity a = getActivityMock();
		Activity a1 = getActivityMock();
		Activity a2 = getActivityMock();
		Activity a3 = getActivityMock();
		activityRepository.save(a);
		activityRepository.save(a1);
		activityRepository.save(a2);
		activityRepository.save(a3);
		getReportMock(a, h);
		getReportMock(a1, h);
		getReportMock(a2, h);
		getReportMock(a3, h);
		a.setDeleted(Boolean.FALSE);
		a1.setDeleted(Boolean.FALSE);
		a2.setDeleted(Boolean.FALSE);
		a3.setDeleted(Boolean.FALSE);

		try {
			assertTrue(hikerService.findHikerFinishActivities(h.getLogin())
					.isEmpty());
			h.getActivities().add(a);
			h.getActivities().add(a1);
			h.getActivities().add(a2);
			h.getActivities().add(a3);
			a.getHikers().add(h);
			a1.getHikers().add(h);
			a2.getHikers().add(h);
			a3.getHikers().add(h);
			assertTrue(hikerService.findHikerFinishActivities(h.getLogin())
					.isEmpty());
			a.setStatus(ActivityStatus.CLOSED);
			assertEquals(1, hikerService.findHikerFinishActivities(h.getLogin())
					.size());
			a1.setStatus(ActivityStatus.CLOSED);
			assertEquals(2, hikerService.findHikerFinishActivities(h.getLogin())
					.size());
			a2.setStatus(ActivityStatus.CLOSED);
			assertEquals(3, hikerService.findHikerFinishActivities(h.getLogin())
					.size());
			a3.setStatus(ActivityStatus.CLOSED);
			assertEquals(4, hikerService.findHikerFinishActivities(h.getLogin())
					.size());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testDeleteHiker() {
		try {
			hikerService.deleteHiker("login");

			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		Hiker h = getHikerMock();
		hikerRepository.save(h);
		try {
			hikerService.deleteHiker(h.getLogin());
			assertTrue(
					hikerService.findHikerByLogin(h.getLogin()).getDeleted());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testResetHikerPassword() {
		try {
			hikerService.resetPassword("login");

			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		Hiker h = getHikerMock();
		hikerRepository.save(h);
		h.setPassword("original");

		try {
			hikerService.resetPassword(h.getEmail());

			fail("Debería haber errores por hiker no activo");
		} catch (BusinessException e) {
		}

		h.setDeleted(Boolean.FALSE);
		h.setActive(Boolean.TRUE);

		try {
			hikerService.resetPassword(h.getEmail());
			fail("Debe haber error al estar en perfil de testing "
					+ "y por tanto, no poder enviar el email con la nueva password");
		} catch (BusinessException e) {
		}

		assertNotEquals("original",
				hikerService.findHikerByEmail(h.getEmail()).getPassword());

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
		act.getReports().add(r);
		hik.getReports().add(r);
		reportRepository.save(r);
		return r;
	}
}
