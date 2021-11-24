package com.bank.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bank.model.User;
import com.bank.repository.UserRepository;
import com.bank.service.LoginAttemptsService;

@Component
public class AccountUnlockScheduler {
	
	@Autowired
	LoginAttemptsService loginAttemptsService;
	
	@Autowired
	UserRepository userRepository;
	
	@Scheduled(fixedRate = 24*60*60)
	public void scheduleFixedRateTask() {
		List<User> usersList = userRepository.findByAccountLockedTrue();
		usersList.forEach(user -> loginAttemptsService.unlockAccount(user));
	}

}