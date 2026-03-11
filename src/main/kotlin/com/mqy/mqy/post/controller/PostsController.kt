package com.mqy.mqy.post.controller

import com.mqy.mqy.common.core.response.ApiResponse
import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.service.PostsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostsController(private val service: PostsService) {
	@GetMapping("/medias/upload-urls")
	fun getMediasUploadUrl(@RequestBody request: MediasUploadUrlDTO): ApiResponse {
		val vo = service.getMediasUploadUrl(request)
		return ApiResponse.success(vo)
	}

	@PostMapping
	fun addPosts(@RequestBody dto: AddPostsDTO): ApiResponse {
		val vo = service.addPosts(dto)
		return ApiResponse.success(vo)
	}

}