package com.mqy.mqy.post

import com.mqy.mqy.common.core.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController {
	@GetMapping("/test")
	fun ddd(): ApiResponse{
		return ApiResponse.success()
	}
}