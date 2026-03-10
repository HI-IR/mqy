package com.mqy.mqy.auth.controller

import com.mqy.mqy.auth.pojo.req.LoginReq
import com.mqy.mqy.auth.pojo.req.RegisterReq
import com.mqy.mqy.auth.service.AuthService
import com.mqy.mqy.common.core.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val service: AuthService) {

	/**
	 * 获取预上传URL
	 */
	@GetMapping("/avatar-upload-url")
	fun getAvatarUploadUrl(): ApiResponse {
		return try {
			ApiResponse.success(service.getAvatarUploadUrl())
		} catch (e: Exception) {
			println(e)
			ApiResponse.error("获取预上传URL错误")
		}
	}

	/**
	 * 登录
	 */
	@PostMapping("/login")
	fun login(@RequestBody req: LoginReq): ApiResponse {
		val vo = service.doLoing(req)
		return ApiResponse.success(vo)
	}

	/**
	 * 注册
	 */
	@PostMapping("/register")
	fun login(@RequestBody req: RegisterReq): ApiResponse {
		val vo = service.doRegister(req)
		return ApiResponse.success(vo)
	}

	@PostMapping("/logout")
	fun logout(): ApiResponse {
		return ApiResponse.success()
	}
}
