package com.abid.social.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abid.social.entity.User;
import com.abid.social.entity.repository.UserRepository;
import com.abid.social.security.user.SocialUser;
import com.abid.social.service.base.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	public SocialUserDetails loadUserByUserId(String userId)
			throws UsernameNotFoundException, DataAccessException {
		User user = userRepository.findOne(Long.valueOf(userId));
		SocialUser socialUser = new SocialUser();
		socialUser.setUserId(userId);
		socialUser.setUserName(user.getUserName());
		socialUser.setProviderId(user.getProviderId());
		socialUser.setProviderUserId(user.getProviderUserId());
		socialUser.setAccessToken(user.getAccessToken());
		return socialUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);
		SocialUser socialUser = new SocialUser();
		socialUser.setUserId(user.getId().toString());
		socialUser.setUserName(user.getUserName());
		socialUser.setProviderId(user.getProviderId());
		socialUser.setProviderUserId(user.getProviderUserId());
		socialUser.setAccessToken(user.getAccessToken());
		return socialUser;
	}

	@Override
	public SocialUser loadUserByConnectionKey(ConnectionKey connectionKey) {

		final User user = userRepository.findByProviderIdAndProviderUserId(
				connectionKey.getProviderId(),
				connectionKey.getProviderUserId());
		if (null == user) {
			return checkUser(null);
		}
		SocialUser socialUser = new SocialUser();
		socialUser.setUserId(user.getId().toString());
		socialUser.setUserName(user.getUserName());
		socialUser.setProviderId(user.getProviderId());
		socialUser.setProviderUserId(user.getProviderUserId());
		socialUser.setAccessToken(user.getAccessToken());
		return checkUser(socialUser);
	}

	@Transactional
	@Override
	public SocialUser saveOrUpdate(SocialUser socialUser) {
		User user = userRepository
				.findOne(Long.valueOf(socialUser.getUserId()));
		user.setAccessToken(socialUser.getAccessToken());
		userRepository.saveAndFlush(user);
		return socialUser;
	}

	private SocialUser checkUser(SocialUser socialUser) {
		if (socialUser == null) {
			throw new UsernameNotFoundException("user not found");
		}
		detailsChecker.check(socialUser);
		return socialUser;
	}

}
