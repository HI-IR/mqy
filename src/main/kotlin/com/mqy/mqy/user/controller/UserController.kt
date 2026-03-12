package com.mqy.mqy.user.controller

import com.mqy.mqy.common.core.response.ApiResponse
import com.mqy.mqy.user.service.impl.UserInfoServiceImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val service: UserInfoServiceImpl) {
	@GetMapping
	fun getUserInfo(): ApiResponse {
		val vo = service.getUserInfo()
		return ApiResponse.success(vo)
	}
}