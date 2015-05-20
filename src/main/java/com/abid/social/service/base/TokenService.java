package com.abid.social.service.base;

import com.abid.social.security.user.SocialUser;

public interface TokenService {

	String encode(byte[] content);

	byte[] decode(String content);

	byte[] toJson(SocialUser socialUser);

	SocialUser fromJson(byte[] content);
}
