package com.abid.social.service.base;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.security.SocialUserDetailsService;

import com.abid.social.security.user.SocialUser;

public interface UserService extends SocialUserDetailsService,
		UserDetailsService {

	SocialUser loadUserByConnectionKey(ConnectionKey connectionKey);

	SocialUser saveOrUpdate(SocialUser socialUser);
}
