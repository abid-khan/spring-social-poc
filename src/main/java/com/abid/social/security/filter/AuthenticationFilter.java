package com.abid.social.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.abid.social.security.constant.Constant;
import com.abid.social.security.user.SocialUser;
import com.abid.social.security.user.SocialUserAuthentication;
import com.abid.social.service.base.TokenService;

@Component
public class AuthenticationFilter extends GenericFilterBean {

	@Autowired
	private TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (null != authentication) {
			if (!(authentication instanceof SocialUserAuthentication)) {
				final SocialUserAuthentication userAuthentication = getAuthentication((HttpServletRequest) request);
				if (userAuthentication != null) {
					SecurityContextHolder.getContext().setAuthentication(
							userAuthentication);
				}
			}

		}

		chain.doFilter(request, response);

	}

	public SocialUserAuthentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(Constant.AUTH_TOKEN);
		if (token != null) {
			final SocialUser socialUser = tokenService.fromJson(tokenService
					.decode(token));
			if (socialUser != null) {
				return new SocialUserAuthentication(socialUser);
			}
		}
		return null;
	}

}
