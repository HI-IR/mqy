package com.mqy.mqy.auth.service.impl

import com.mqy.mqy.auth.mapper.AuthMapper
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * 专门用作密码验证的Service
 */
@Component
class UserServiceImpl(private val mapper: AuthMapper) : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val userEntity = mapper.getUserByUsername(username)
			?: throw UsernameNotFoundException("用户未找到")
		return User.withUsername(userEntity.username).password(userEntity.passwordHash).roles(userEntity.role).build()
	}
}