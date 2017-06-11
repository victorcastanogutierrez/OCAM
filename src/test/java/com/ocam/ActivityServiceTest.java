package com.ocam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.model.types.GPSPoint;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;
import com.ocam.service.ActivityService;

@Rollback(true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private HikerRepository hikerRepository;

	@Autowired
	private ActivityRepository activityRepository;

	private int hikers = 0;

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testSave() {
		ActivityHikerDTO actDTO = new ActivityHikerDTO();
		Hiker hiker = new Hiker();

		expectActivitySaveError(actDTO); // No hay hiker

		actDTO.setHiker(hiker);
		expectActivitySaveError(actDTO); // Hiker sin login

		hiker.setLogin("login");
		expectActivitySaveError(actDTO); // Hiker no persistido

		hiker = getHikerMock();
		hikerRepository.save(hiker);
		actDTO.setHiker(hiker);

		Activity act = getActivityMock();
		activityRepository.save(act);
		actDTO.setActivity(act);

		expectActivitySaveError(actDTO); // Error de permisos para edición

		act.setOwner(hiker);
		actDTO.setRequestUser(hiker.getLogin());
		Hiker guia = getHikerMock();
		act.getGuides().add(guia);
		expectActivitySaveError(actDTO); // Guía no persistido
		hikerRepository.save(guia);

		act.setMide("asd");
		expectActivitySaveError(actDTO); // MIDE invalido

		act.setMide("http://www.google.es");
		act.setDeleted(Boolean.TRUE);
		act.setStatus(ActivityStatus.RUNNING);
		expectActivitySaveError(actDTO); // Intento de eliminar estando en curso

		act.setStatus(ActivityStatus.PENDING);
		try {
			activityService.saveActivity(actDTO);
		} catch (BusinessException e) {
			assertNull(e); // Actualizacion correcta
		}

		act = getActivityMock();
		act.getGuides().add(guia);
		try {
			activityService.saveActivity(actDTO);
		} catch (BusinessException e) {
			assertNull(e); // Guardado correcto
		}

		assertNotNull(
				activityService.findActivityById(actDTO.getActivity().getId()));
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindLastActivityReports() {
		Activity act = getActivityMock();

		try {
			activityService.findLastActivityReports(act.getId());
			fail("Se esperaba error: id null");
		} catch (BusinessException e) {
		}

		activityRepository.save(act);
		Hiker h = getHikerMock();
		Hiker h1 = getHikerMock();
		Hiker h2 = getHikerMock();
		hikerRepository.save(h);
		hikerRepository.save(h1);
		hikerRepository.save(h2);

		try {
			Set<Report> reports = activityService
					.findLastActivityReports(act.getId());
			assertTrue(reports.isEmpty());
			act.getReports().add(getReportMock(act, h));
			act.getReports().add(getReportMock(act, h1));
			act.getReports().add(getReportMock(act, h2));
			reportRepository.save(act.getReports());

			reports = activityService.findLastActivityReports(act.getId());
			assertEquals(0, reports.size()); // No están unidos en la actividad

			act.getHikers().add(h);
			act.getHikers().add(h1);
			act.getHikers().add(h2);
			h.getActivities().add(act);
			h1.getActivities().add(act);
			h2.getActivities().add(act);

			reports = activityService.findLastActivityReports(act.getId());
			assertEquals(3, reports.size());

			Calendar c = Calendar.getInstance(); // Prueba que se quede con el
													// report mas reciente
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, 10);
			Report posterior = getReportMock(act, h1);
			posterior.setDate(c.getTime());
			reportRepository.save(posterior);
			act.getReports().add(posterior);

			reports = activityService.findLastActivityReports(act.getId());
			assertEquals(3, reports.size()); // Solo uno por hiker

		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindAllPendingActivities() {
		try {
			activityService.findAllPendingActivities(null);
			fail("Se espera error: sin criterios de búsqueda");
		} catch (BusinessException e) {
		}

		ActivityDTO actDTO = new ActivityDTO();

		try {
			activityService.findAllPendingActivities(actDTO);
			fail("Se espera error: criterios inválidos");
		} catch (BusinessException e) {
		}

		actDTO.setMaxResults(Integer.MAX_VALUE);
		actDTO.setMinResults(0);
		try {
			assertTrue(
					activityService.findAllPendingActivities(actDTO).isEmpty());

			int r = new Random().nextInt(10) + 5;
			for (int i = 0; i < r; i++) {
				activityRepository.save(getActivityMock());
			}

			assertEquals(r,
					activityService.findAllPendingActivities(actDTO).size());

			actDTO.setMaxResults(1);
			assertEquals(1,
					activityService.findAllPendingActivities(actDTO).size());

		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindActivityReportsByHiker() {
		assertNull(activityService.findActivityReportsByHiker(null, null));
		assertNull(activityService.findActivityReportsByHiker(10L, null));
		assertNull(activityService.findActivityReportsByHiker(null, "email"));
		assertNull(activityService.findActivityReportsByHiker(10L, "email"));

		Activity act = getActivityMock();
		Hiker hik = getHikerMock();

		assertNull(activityService.findActivityReportsByHiker(act.getId(),
				hik.getEmail()));
		activityRepository.save(act);
		assertNull(activityService.findActivityReportsByHiker(act.getId(),
				hik.getEmail()));
		hikerRepository.save(hik);
		assertTrue(activityService
				.findActivityReportsByHiker(act.getId(), hik.getEmail())
				.isEmpty());

		Report r = getReportMock(act, hik);
		reportRepository.save(r);
		hik.getReports().add(r);
		act.getReports().add(r);
		assertEquals(1,
				activityService
						.findActivityReportsByHiker(act.getId(), hik.getEmail())
						.size());
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testJoinActivityHiker() {
		try {
			activityService.joinActivityHiker(10L, "login", "password");

			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		String password_valida = "password_valida";
		String password_invalida = "password_invalida";
		Hiker h = getHikerMock();
		Activity a = getActivityMock();
		hikerRepository.save(h);
		activityRepository.save(a);

		try {
			a.setPassword(password_valida);
			activityService.joinActivityHiker(a.getId(), h.getLogin(),
					password_invalida);

			fail("Debería haber error por password invalida");
		} catch (BusinessException e) {
		}

		h.getActivities().add(a);
		a.getHikers().add(h);

		try {
			h.setPassword(password_valida);
			activityService.joinActivityHiker(a.getId(), h.getLogin(),
					password_valida);

			fail("Debería haber error: hiker ya unido");
		} catch (BusinessException e) {
		}

		h.getActivities().remove(a);
		a.getHikers().remove(h);

		Activity a2 = getActivityMock();
		activityRepository.save(a2);
		a2.setStatus(ActivityStatus.RUNNING);

		h.getActivities().add(a2);
		a2.getHikers().add(h);

		try {
			h.setPassword(password_valida);
			activityService.joinActivityHiker(a.getId(), h.getLogin(),
					password_valida);

			fail("Debería haber error: hiker ya unido en una actividad en curso");
		} catch (BusinessException e) {
		}

		a2.setStatus(ActivityStatus.CLOSED);

		try {
			h.setPassword(password_valida);
			activityService.joinActivityHiker(a.getId(), h.getLogin(),
					password_valida);
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testCloseActivity() {
		try {
			activityService.closeActivity(10L);

			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		Activity a = getActivityMock();
		activityRepository.save(a);
		a.setDeleted(Boolean.TRUE);

		try {
			activityService.closeActivity(a.getId());

			fail("Debería haber errores por actividad eliminada");
		} catch (BusinessException e) {
		}

		a.setDeleted(Boolean.FALSE);
		try {
			activityService.closeActivity(a.getId());
		} catch (BusinessException e) {
			fail("No debería haber errores.");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testCheckActivityPassword() {
		ActivityDTO aDTO = new ActivityDTO();
		aDTO.setId(10L);

		try {
			activityService.checkActivityPassword(aDTO);

			fail("Debería haber errores por actividad inexistente");
		} catch (BusinessException e) {
		}

		Activity a = getActivityMock();
		activityRepository.save(a);
		aDTO.setId(a.getId());

		try {
			activityService.checkActivityPassword(aDTO);

			fail("Debería haber errores por password inexistente");
		} catch (BusinessException e) {
		}

		aDTO.setPassword("password");
		try {
			activityService.checkActivityPassword(aDTO);
			fail("Debería haber errores por password aún no asociada a actividad");
		} catch (BusinessException e) {
		}

		a.setPassword("password");
		aDTO.setPassword("invalida");
		try {
			activityService.checkActivityPassword(aDTO);

			fail("Debería haber errores por password invalida");
		} catch (BusinessException e) {
		}

		aDTO.setPassword("password");
		try {
			activityService.checkActivityPassword(aDTO);
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindActivityById() {
		assertNull(activityService.findActivityById(null));
		assertNull(activityService.findActivityById(10L));
		Activity a = getActivityMock();
		activityRepository.save(a);
		assertNotNull(activityService.findActivityById(a.getId()));
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testStartActivity() {
		try {
			activityService.startActivity(null, null, null);

			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		Hiker h = getHikerMock();
		Activity a = getActivityMock();
		hikerRepository.save(h);
		activityRepository.save(a);

		try {
			activityService.startActivity(a.getId(), "password", h.getLogin());
			fail("Debeía haber errores por no ser guía de la actividad");
		} catch (BusinessException e) {
		}

		a.getGuides().add(h);
		h.getActivityGuide().add(a);

		try {
			activityService.startActivity(a.getId(), "password", h.getLogin());
			ActivityDTO aDTO = new ActivityDTO();
			aDTO.setId(a.getId());
			aDTO.setPassword("password");
			activityService.checkActivityPassword(aDTO);
			assertEquals(
					activityService.findActivityById(a.getId()).getStatus(),
					ActivityStatus.RUNNING);
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testUpdateActivityPassword() {
		ActivityHikerDTO aDTO = new ActivityHikerDTO();
		Activity a = getActivityMock();
		Hiker h = getHikerMock();
		activityRepository.save(a);

		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		aDTO.setActivity(a);
		aDTO.setRequestUser("victor");
		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por password invalida");
		} catch (BusinessException e) {
		}

		a.setPassword("a");
		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por password invalida");
		} catch (BusinessException e) {
		}

		a.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por password invalida");
		} catch (BusinessException e) {
		}

		a.setPassword("aaaaaa");
		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por hiker inexistente");
		} catch (BusinessException e) {
		}

		hikerRepository.save(h);
		aDTO.setRequestUser(h.getLogin());
		try {
			activityService.updateActivityPassword(aDTO);
			fail("Debería haber errores por hiker no guía");
		} catch (BusinessException e) {
		}

		h.getActivityGuide().add(a);
		a.getGuides().add(h);
		try {
			activityService.updateActivityPassword(aDTO);
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testFindACtivityHikers() {

		Hiker h = getHikerMock();
		Hiker h2 = getHikerMock();
		Hiker h1 = getHikerMock();
		Hiker h3 = getHikerMock();
		Activity a = getActivityMock();
		activityRepository.save(a);
		hikerRepository.save(h);
		hikerRepository.save(h1);
		hikerRepository.save(h2);
		hikerRepository.save(h3);

		try {
			activityService.findActivityHikers(null);
			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		try {
			activityService.findActivityHikers(5L);
			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		a.getHikers().add(h);
		a.getHikers().add(h1);
		a.getHikers().add(h2);
		a.getHikers().add(h3);
		h.getActivities().add(a);
		h1.getActivities().add(a);
		h2.getActivities().add(a);
		h3.getActivities().add(a);

		try {
			assertEquals(4,
					activityService.findActivityHikers(a.getId()).size());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	@Test
	@Rollback(true)
	@Transactional(readOnly = false)
	public void testDeleteActivityHiker() {
		try {
			activityService.deleteActivityHiker(10L, "victor");
			fail("Debería haber errores por argumentos inválidos");
		} catch (BusinessException e) {
		}

		Hiker h = getHikerMock();
		Activity a = getActivityMock();
		activityRepository.save(a);
		hikerRepository.save(h);

		a.setStatus(ActivityStatus.PENDING);
		try {
			activityService.deleteActivityHiker(a.getId(), h.getLogin());
			fail("Debería haber errores por actividad pendiente");
		} catch (BusinessException e) {
		}

		a.setStatus(ActivityStatus.RUNNING);
		try {
			activityService.deleteActivityHiker(a.getId(), h.getLogin());
			fail("Debería haber errores por no pertenecer a la actividad");
		} catch (BusinessException e) {
		}

		a.setPassword("password");
		try {
			activityService.joinActivityHiker(a.getId(), h.getLogin(),
					"password");
			assertFalse(
					activityService.findActivityHikers(a.getId()).isEmpty());
			activityService.deleteActivityHiker(a.getId(), h.getLogin());
			assertTrue(activityService.findActivityHikers(a.getId()).isEmpty());
		} catch (BusinessException e) {
			fail("No debería haber errores");
		}
	}

	private void expectActivitySaveError(ActivityHikerDTO actDTO) {
		try {
			activityService.saveActivity(actDTO);
			fail("Se esperaba error");
		} catch (BusinessException e) {
		}
	}

	private Activity getActivityMock() {
		Activity activity = new Activity();
		activity.setShortDescription("Descrt");
		activity.setTrack("track");
		activity.setStartDate(new Date());
		activity.setStatus(ActivityStatus.PENDING);
		return activity;
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
