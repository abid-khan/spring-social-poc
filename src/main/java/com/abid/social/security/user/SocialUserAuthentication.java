package com.abid.social.security.user;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class SocialUserAuthentication implements Authentication {

	private SocialUser socialUser;

	private boolean authenticated = true;

	public SocialUserAuthentication(SocialUser socialUser) {
		this.socialUser = socialUser;
	}

	public SocialUser getSocialUser() {
		return socialUser;
	}

	public void setSocialUser(SocialUser socialUser) {
		this.socialUser = socialUser;
	}

	@Override
	public String getName() {
		return this.socialUser.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDetails() {
		return socialUser;
	}

	@Override
	public Object getPrincipal() {
		return socialUser;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}
