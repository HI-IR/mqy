package com.mqy.mqy.common.config.filter

import com.mqy.mqy.auth.service.impl.UserServiceImpl
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity // 接管安全验证
class SecurityConfig(
	private val jwtFilter: JwtFilter,
	private val userService: UserServiceImpl
) {
	private val permitUrl = listOf(
		HttpMethod.GET to "/auth/avatar-upload-url",
		HttpMethod.POST to "/auth/login",
		HttpMethod.POST to "/auth/register",
		HttpMethod.GET to "/posts/*",
		HttpMethod.POST to "/suggest",
		HttpMethod.GET to "/home/posts"
	)

	private val adminUrl = listOf(
		HttpMethod.GET to "/cats/avatar-upload-url",
		HttpMethod.POST to "/cats"
	)

	@Bean
	fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
		return config.authenticationManager
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	fun authenticationProvider(): AuthenticationProvider {
		val provider = DaoAuthenticationProvider(userService)
		provider.setPasswordEncoder(passwordEncoder())
		return provider
	}

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.authenticationProvider(authenticationProvider())
			.exceptionHandling {
				it.authenticationEntryPoint { _, response, _ ->
					response.status = HttpServletResponse.SC_UNAUTHORIZED
					response.contentType = "application/json;charset=UTF-8"
					response.writer.write("""{"code":401,"msg":"未登录，请先登录","data":null}""")
				}
				it.accessDeniedHandler { _, response, _ ->
					response.status = HttpServletResponse.SC_FORBIDDEN
					response.contentType = "application/json;charset=UTF-8"
					response.writer.write("""{"code":403,"msg":"权限不足","data":null}""")
				}
			}
			.authorizeHttpRequests { auth ->
				permitUrl.forEach { (method, url) ->
					auth.requestMatchers(method, url).permitAll()
				}
				adminUrl.forEach { (method, url) ->
					auth.requestMatchers(method, url).hasAuthority("ADMIN")
				}
				auth.anyRequest().authenticated()
			}
			// 把我的拦截器添加到UsernamePasswordAuthenticationFilter之前
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
		return http.build()
	}
}
