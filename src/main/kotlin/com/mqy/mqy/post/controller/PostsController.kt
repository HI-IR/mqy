package com.mqy.mqy.post.controller

import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.common.core.response.ApiResponse
import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.service.PostsService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostsController(private val service: PostsService) {
	@PostMapping("/medias/upload-urls")
	fun getMediasUploadUrl(@RequestBody request: MediasUploadUrlDTO): ApiResponse {
		val vo = service.getMediasUploadUrl(request)
		return ApiResponse.success(vo)
	}

	@PostMapping
	fun addPosts(@RequestBody dto: AddPostsDTO): ApiResponse {
		val vo = service.addPosts(dto)
		return ApiResponse.success(vo)
	}

	@GetMapping("/{postId}")
	suspend fun getPostDetail(@PathVariable postId: Long): ApiResponse {
		val userIdLong =
			(SecurityContextHolder.getContext().authentication?.principal as? CustomUserDetail)?.id?.toLong() ?: -1L
		val vo = service.getPostDetail(userIdLong, postId)
		return ApiResponse.success(vo)
	}

	@GetMapping("/suggest")
	fun postsSearchCat(
		@RequestParam("keyword") keyword: String,
		@RequestParam("limit", required = false, defaultValue = "10") limit: Long
	): ApiResponse {
		val postsSearchCat = service.postsSearchCat(keyword, limit)
		return ApiResponse.success(postsSearchCat)
	}

	@PostMapping("/like")
	fun likePost(@RequestParam id: Long): ApiResponse {
		service.likePost(id)
		return ApiResponse.success()
	}

	@PostMapping("/unlike")
	fun unlikePost(@RequestParam id: Long): ApiResponse {
		service.unLikePost(id)
		return ApiResponse.success()
	}

	@DeleteMapping("/{postId}")
	fun deletePost(@PathVariable postId: Long): ApiResponse {
		service.deletePost(postId)
		return ApiResponse.success("删除成功")
	}
}