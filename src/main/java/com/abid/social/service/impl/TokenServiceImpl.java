package com.abid.social.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.abid.social.security.user.SocialUser;
import com.abid.social.service.base.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenServiceImpl implements TokenService {

	@Override
	public String encode(byte[] content) {
		return new String(Base64.encode(content));
	}

	@Override
	public byte[] decode(String content) {
		return content.getBytes();
	}

	@Override
	public byte[] toJson(SocialUser socialUser) {
		try {
			return new ObjectMapper().writeValueAsBytes(socialUser);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public SocialUser fromJson(byte[] content) {
		try {
			return new ObjectMapper().readValue(new ByteArrayInputStream(
					content), SocialUser.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

}
