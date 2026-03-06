package com.mqy.mqy.common.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
	val permitUrl = mutableListOf("/auth/login", "/auth/register", "/auth/avatar-upload-url")

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.authorizeHttpRequests { auth ->
				auth
					.requestMatchers(*permitUrl.toTypedArray()).permitAll()
					.anyRequest().authenticated()
			}

		return http.build()
	}
}