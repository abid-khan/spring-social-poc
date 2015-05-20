package com.abid.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import com.abid.social.security.filter.AuthenticationFilter;
import com.abid.social.security.handler.SocialAuthenticationSuccessHandler;
import com.abid.social.service.base.UserService;

@Configuration
@EnableWebMvcSecurity
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationFilter authenticationFilter;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UserIdSource userIdSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		final SpringSocialConfigurer socialConfigurer = new SpringSocialConfigurer();
		socialConfigurer.postLoginUrl("/home");
		socialConfigurer.alwaysUsePostLoginUrl(true);
		socialConfigurer.userIdSource(userIdSource);

		socialConfigurer
				.addObjectPostProcessor(new ObjectPostProcessor<SocialAuthenticationFilter>() {

					@Override
					public <O extends SocialAuthenticationFilter> O postProcess(
							O socialAuthenticationFilter) {
						socialAuthenticationFilter
								.setAuthenticationSuccessHandler(authenticationSuccessHandler());
						return socialAuthenticationFilter;
					}
				});

		http.exceptionHandling()
				.and()
				.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/")
				.permitAll()
				.antMatchers("auth/**")
				.permitAll()
				.antMatchers("/api/**")
				.authenticated()
				.and()
				.addFilterBefore(authenticationFilter,
						AbstractPreAuthenticatedProcessingFilter.class)
				.apply(socialConfigurer);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userService);
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userService;
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SocialAuthenticationSuccessHandler();
	}

}
