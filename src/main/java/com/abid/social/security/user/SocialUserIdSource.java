package com.abid.social.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;

public class SocialUserIdSource implements UserIdSource {

	@Override
	public String getUserId() {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();

		if (authentication == null) {
			throw new IllegalStateException(
					"Unable to get a ConnectionRepository: no user signed in");
		}

		SocialUser socialUser = null;
		if (authentication instanceof SocialUserAuthentication) {
			socialUser = (SocialUser) authentication.getPrincipal();
		}

		if (socialUser == null) {
			throw new IllegalStateException(
					"Unable to get a ConnectionRepository: no user signed in");
		}
		return socialUser.getUserId();
	}

}
