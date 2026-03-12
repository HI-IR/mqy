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
		@RequestParam("limit", required = false, defaultValue = "10") limit: Int,
		@RequestParam("keyword", required = false) keyword: String?
	): ApiResponse {
		val userIdLong =
			(SecurityContextHolder.getContext().authentication?.principal as? CustomUserDetail)?.id?.toLong() ?: -1L
		val homePosts = service.getHomePosts(userIdLong, cursor, limit,keyword)
		return ApiResponse.success(homePosts)
	}

	@GetMapping("/gallery")
	suspend fun getGallery(
		@RequestParam("cursor", required = false) cursor: Long?,
		@RequestParam("limit", required = false, defaultValue = "10") limit: Int,
		@RequestParam("state", required = false) state: Int?,
		@RequestParam("keyword", required = false) keyword: String?
	): ApiResponse {
		val galleryVO = service.getGallery(cursor, limit, state, keyword)
		return ApiResponse.success(galleryVO)

	}

}