package com.mqy.mqy.common.config.cors

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * 全局跨域配置
 */
@Configuration
class GlobalCorsConfig {

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource {
		val config = CorsConfiguration().apply {
			allowCredentials = true
			addAllowedOriginPattern("*")
			addAllowedHeader("*")
			addAllowedMethod("*")
			maxAge = 3600L
		}

		return UrlBasedCorsConfigurationSource().apply {
			registerCorsConfiguration("/**", config)
		}
	}
}