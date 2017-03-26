package com.ocam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.HikerService;

@Rollback(true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class HikerServiceTest {

	final Logger log = Logger.getLogger(HikerServiceTest.class.getName());

	private final String login = "testLogin";
	private final String passwd = "passwd";
	private final String email = "emailTest";

	@Autowired
	private HikerService hikerService;

	@DirtiesContext
	@Test
	public void testHikerSave() {
		// Creates the new hiker and persists it
		HikerDTO hdto = new HikerDTO();
		hdto.setUsername(login);
		hdto.setPassword(passwd);
		hdto.setEmail(email);

		Hiker nhiker = null;
		try {
			hikerService.saveHiker(hdto);
			nhiker = hikerService.findHikerByEmail(hdto.getEmail());

			// Retrieves the created hiker and checks if everything is ok
			nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		} catch (BusinessException e) {
			assertNull(e);
		}

		assertNotNull(nhiker);
		assertEquals(login, nhiker.getLogin());
		assertEquals(email, nhiker.getEmail());
	}

	@DirtiesContext
	@Test
	public void testHikerUpdate() {
		// Creates the new hiker
		HikerDTO hdto = new HikerDTO();
		hdto.setUsername(login);
		hdto.setPassword(passwd);
		hdto.setEmail(email);

		Hiker nhiker = null;
		try {
			// Persists it
			hikerService.saveHiker(hdto);
			nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		} catch (BusinessException e) {
			assertNull(e);
		}

		assertNotNull(nhiker);

		// Updates it
		nhiker.setLogin("log");
		nhiker.setEmail("em");
		nhiker.setPassword("pas");

		try {
			hikerService.updateHiker(nhiker);

			// Retrieves it again
			nhiker = hikerService.findHikerByLoginPassword("log", "pas");
		} catch (BusinessException e) {
			assertNull(e);
		}
		assertNotNull(nhiker);

		// Checks if everything is ok
		assertNotNull(nhiker);
		assertEquals("log", nhiker.getLogin());
		assertEquals("em", nhiker.getEmail());

		// Checks if it is duplicated
		try {
			nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		} catch (BusinessException e) {
			assertNull(e);
		}
		assertNull(nhiker);
	}

}
