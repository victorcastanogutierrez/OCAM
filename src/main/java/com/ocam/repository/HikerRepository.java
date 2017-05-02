package com.ocam.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;

public interface HikerRepository extends JpaRepository<Hiker, Long> {

	Hiker findByLoginAndPassword(String name, String password);

	Hiker findByLogin(String login);

	Hiker findTopByLoginOrEmail(String login, String email);

	Hiker findByEmail(String email);

	Hiker findTopByActiveCodeAndActive(String ActiveCode, Boolean Active);

	@Query("select distinct a from Activity a join a.reports r join r.hiker h where h.login = :login and a.status = 'CLOSED' and a.deleted = false")
	Set<Activity> findHikerActivities(@Param("login") String login);

}
