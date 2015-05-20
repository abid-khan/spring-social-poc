package com.abid.social.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abid.social.entity.User;
import com.abid.social.entity.repository.UserRepository;

@Component("socialSignupHandler")
public class SocialSignupHandler implements ConnectionSignUp {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public String execute(Connection<?> connection) {
		logger.info("Add new user to system");
		User user = new User();
		user.setUserName(connection.fetchUserProfile().getFirstName());
		user.setProviderId(connection.getKey().getProviderId());
		user.setProviderUserId(connection.getKey().getProviderUserId());
		user.setAccessToken(connection.createData().getAccessToken());
		user = userRepository.saveAndFlush(user);
		return user.getId().toString();
	}

}
