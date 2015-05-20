package com.abid.social.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abid.social.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Transactional(readOnly = true)
	User findByProviderIdAndProviderUserId(String providerId,
			String providerUserId);

	@Transactional(readOnly = true)
	User findByUserName(String userName);
}
