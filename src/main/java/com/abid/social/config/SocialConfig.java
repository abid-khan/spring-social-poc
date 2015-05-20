package com.abid.social.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfiguration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import com.abid.social.security.repository.SocialUsersConnectionRepository;
import com.abid.social.security.user.SocialUserIdSource;
import com.abid.social.service.base.UserService;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfiguration {

	@Autowired
	@Resource(name = "socialSignupHandler")
	private ConnectionSignUp connectionSignUp;

	@Autowired
	private UserService userService;

	@Bean
	public UserIdSource userIdSource() {
		return new SocialUserIdSource();
	}

	@Override
	public UsersConnectionRepository usersConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator) {
		SocialUsersConnectionRepository repository = new SocialUsersConnectionRepository(
				userService, connectionFactoryLocator);
		repository.setConnectionSignUp(connectionSignUp);
		return repository;
	}

}
