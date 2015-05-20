package com.abid.social.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.abid.social.security.constant.Constant;
import com.abid.social.security.user.SocialUser;
import com.abid.social.security.user.SocialUserAuthentication;
import com.abid.social.service.base.TokenService;
import com.abid.social.service.base.UserService;

public class SocialAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		SocialUser socilaUser = (SocialUser) userService
				.loadUserByUsername(authentication.getName());
		final SocialUserAuthentication userAuthentication = new SocialUserAuthentication(
				socilaUser);
		addAuthentication(response, userAuthentication);
		super.onAuthenticationSuccess(request, response, userAuthentication);
	}

	public void addAuthentication(HttpServletResponse response,
			SocialUserAuthentication authentication) {
		final SocialUser socialUser = (SocialUser) authentication.getDetails();
		final String token = tokenService.encode(tokenService
				.toJson(socialUser));
		response.addHeader(Constant.AUTH_TOKEN, token);
	}

}
