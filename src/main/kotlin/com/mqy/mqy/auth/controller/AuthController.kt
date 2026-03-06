package com.mqy.mqy.auth.controller

import com.mqy.mqy.auth.service.AuthService
import com.mqy.mqy.common.core.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.log

@RestController
@RequestMapping("/auth")
class AuthController(private val service: AuthService) {
	@GetMapping("/avatar-upload-url")
	fun getAvatarUploadUrl(): ApiResponse {
		return try {
			ApiResponse.success(service.getAvatarUploadUrl())
		} catch (e: Exception) {
			println(e)
			ApiResponse.error("获取预上传URL错误")
		}
	}


}
