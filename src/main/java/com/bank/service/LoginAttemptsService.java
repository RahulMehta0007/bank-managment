package com.bank.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.model.User;
import com.bank.repository.UserRepository;

@Service
public class LoginAttemptsService {

	private static final int MAX_ATTEMPTS_ALLOWED = 3;
	private static final long LOCK_DURATION = 24 * 60 * 60;
	@Autowired
	UserRepository userRepository;

	public void loginSucceeded(String key) {
		Optional<User> user = userRepository.findByUsername(key);
		user.ifPresent(u -> u.setFailedAttempt(0));
		userRepository.save(user.get());
	}

	public void loginFailed(String key) {
		Optional<User> userOpt = userRepository.findByUsername(key);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			int currentFailAttempts = user.getFailedAttempt();
			if (currentFailAttempts >= MAX_ATTEMPTS_ALLOWED) {
				this.lockAccount(user);
				throw new LockedException("ACCOUNT_LOCKED");
			} else {
				user.setFailedAttempt(currentFailAttempts + 1);
				userRepository.save(user);
				throw new BadCredentialsException("BAD_CRED");
			}
		}
	}

	public boolean isAccountLocked(String key) {
		Optional<User> userOpt = userRepository.findByUsername(key);
		if (userOpt.isPresent()) {
			return userOpt.get().isAccountLocked();
		} else
			throw new UsernameNotFoundException(key);
	}

	public void lockAccount(User user) {

		user.setAccountLocked(true);
		user.setLockTime(new Date());
		userRepository.save(user);

	}

	public boolean unlockAccount(User user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeInMillis = System.currentTimeMillis();

		if (lockTimeInMillis + LOCK_DURATION > currentTimeInMillis) {
			user.setAccountLocked(false);
			user.setFailedAttempt(0);
			user.setLockTime(null);

			try {
				userRepository.save(user);
				return true;
			} catch (Exception e) {
				// throw new
				// RequestProcessingFailedException(Constants.ATTEMPT_DB_UPDATE_FAILED);
			}
		}
		return false;
	}

}
