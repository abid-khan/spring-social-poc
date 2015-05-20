package com.abid.social.security.repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.AuthenticationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.SocialInMemoryConnectionRepository;
import org.springframework.transaction.annotation.Transactional;

import com.abid.social.security.user.SocialUser;
import com.abid.social.service.base.UserService;

public class SocialUsersConnectionRepository implements
		UsersConnectionRepository {

	private UserService userService;

	private ConnectionFactoryLocator connectionFactoryLocator;

	private ConnectionSignUp connectionSignUp;

	public SocialUsersConnectionRepository(UserService userService,
			ConnectionFactoryLocator connectionFactoryLocator) {
		this.userService = userService;
		this.connectionFactoryLocator = connectionFactoryLocator;

	}

	@Transactional
	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		try {
			SocialUser socialUser = userService
					.loadUserByConnectionKey(connection.getKey());
			socialUser.setAccessToken(connection.createData().getAccessToken());
			userService.saveOrUpdate(socialUser);
			return Arrays.asList(socialUser.getUserId());
		} catch (AuthenticationException ae) {
			return Arrays.asList(connectionSignUp.execute(connection));
		}
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds) {
		Set<String> keys = new HashSet<>();
		for (String userId : providerUserIds) {
			ConnectionKey ck = new ConnectionKey(providerId, userId);
			try {
				keys.add(userService.loadUserByConnectionKey(ck).getUserId());
			} catch (AuthenticationException ae) {
				// ignore
			}
		}
		return keys;
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		final ConnectionRepository connectionRepository = new SocialInMemoryConnectionRepository(
				connectionFactoryLocator);
		final SocialUser socialUser = (SocialUser) userService
				.loadUserByUserId(userId);
		final ConnectionData connectionData = new ConnectionData(
				socialUser.getProviderId(), socialUser.getProviderUserId(),
				null, null, null, socialUser.getAccessToken(), null, null, null);

		final Connection connection = connectionFactoryLocator
				.getConnectionFactory(socialUser.getProviderId())
				.createConnection(connectionData);
		connectionRepository.addConnection(connection);
		return connectionRepository;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ConnectionFactoryLocator getConnectionFactoryLocator() {
		return connectionFactoryLocator;
	}

	public void setConnectionFactoryLocator(
			ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	public ConnectionSignUp getConnectionSignUp() {
		return connectionSignUp;
	}

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}

}
