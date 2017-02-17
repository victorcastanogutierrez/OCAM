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
import org.springframework.test.context.junit4.SpringRunner;

import com.ocam.model.Hiker;
import com.ocam.service.HikerService;

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
		//Creates the new hiker and persists it
		Hiker nhiker = new Hiker();
		nhiker.setLogin(login);
		nhiker.setPassword(passwd);
		nhiker.setEmail(email);
		hikerService.saveHiker(nhiker);
		
		//Retrieves the created hiker and checks if everything is ok
		nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		assertNotNull(nhiker);
		assertEquals(login, nhiker.getLogin());
		assertEquals(passwd, nhiker.getPassword());
		assertEquals(email, nhiker.getEmail());
	}
	
	@DirtiesContext
	@Test
	public void testHikerUpdate() {
		//Creates the new hiker
		Hiker nhiker = new Hiker();
		nhiker.setLogin(login);
		nhiker.setPassword(passwd);
		nhiker.setEmail(email);
		hikerService.saveHiker(nhiker);
		
		//Persists it
		nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		assertNotNull(nhiker);
		
		//Updates it
		nhiker.setLogin("log");
		nhiker.setEmail("em");
		nhiker.setPassword("pas");
		hikerService.updateHiker(nhiker);
		
		//Retrieves it again
		nhiker = hikerService.findHikerByLoginPassword("log", "pas");
		assertNotNull(nhiker);
		
		//Checks if everything is ok
		assertNotNull(nhiker);
		assertEquals("log", nhiker.getLogin());
		assertEquals("pas", nhiker.getPassword());
		assertEquals("em", nhiker.getEmail());
		
		//Checks if it is duplicated
		nhiker = hikerService.findHikerByLoginPassword(login, passwd);
		assertNull(nhiker);
	}

}
