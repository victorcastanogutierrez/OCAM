package com.ocam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Hiker;

public interface HikerRepository extends JpaRepository<Hiker, Long> {

	Hiker findByLoginAndPassword(String name, String password);

	Hiker findByLogin(String login);

	Hiker findTopByLoginOrEmail(String login, String email);

}
