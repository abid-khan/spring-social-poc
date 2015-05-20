package org.springframework.social.connect.mem;

import org.springframework.social.connect.ConnectionFactoryLocator;

public class SocialInMemoryConnectionRepository extends InMemoryConnectionRepository {

	public SocialInMemoryConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator) {
		super(connectionFactoryLocator);
	}
}
