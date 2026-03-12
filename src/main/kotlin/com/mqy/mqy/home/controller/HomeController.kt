package com.mqy.mqy.home.controller

import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.common.core.response.ApiResponse
import com.mqy.mqy.home.service.HomeService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class HomeController(private val service: HomeService) {

	@GetMapping("/posts")
	suspend fun getHomePosts(
		@RequestParam("cursor", required = false) cursor: Long?,
		@RequestParam("limit", required = false, defaultValue = "10") limit: Int
	): ApiResponse {
		val userIdLong =
			(SecurityContextHolder.getContext().authentication?.principal as? CustomUserDetail)?.id?.toLong() ?: -1L
		val homePosts = service.getHomePosts(userIdLong, cursor, limit)
		return ApiResponse.success(homePosts)
	}

}