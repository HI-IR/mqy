package com.mqy.mqy.cats.controller

import com.mqy.mqy.cats.pojo.dto.AddCatsDTO
import com.mqy.mqy.cats.pojo.dto.AdoptCatDTO
import com.mqy.mqy.cats.service.CatsService
import com.mqy.mqy.common.core.response.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cats")
class CatsController(private val service: CatsService) {
	@GetMapping("/avatar-upload-url")
	fun getAvatarUpload(): ApiResponse {
		val vo = service.getAvatarUploadUrl()
		return ApiResponse.success(vo)
	}

	@PostMapping
	fun addCats(@RequestBody addCatsDTO: AddCatsDTO): ApiResponse {
		val addCats = service.addCats(addCatsDTO)
		return ApiResponse.success(addCats)
	}

	@PostMapping("/follow")
	fun followCats(@RequestParam(value = "id", required = true) id: Long): ApiResponse {
		service.followCat(id)
		return ApiResponse.success()
	}

	@PostMapping("/unfollow")
	fun unfollowCats(@RequestParam(value = "id", required = true) id: Long): ApiResponse {
		service.unfollowCat(id)
		return ApiResponse.success()
	}

	/**
	 * 宠物详情
	 */
	@GetMapping("/{catId}")
	fun getCatDetail(@PathVariable catId: Long): ApiResponse {
		val catDetail = service.getCatDetail(catId)
		return ApiResponse.success(catDetail)
	}

	@PostMapping("/adopt")
	fun adoptCat(@RequestBody adoptCatDTO: AdoptCatDTO): ApiResponse {
		service.adoptCat(adoptCatDTO)
		return ApiResponse.success()
	}
}