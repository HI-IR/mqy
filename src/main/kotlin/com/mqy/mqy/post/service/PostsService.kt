package com.mqy.mqy.post.service

import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.pojo.vo.AddPostsVO
import com.mqy.mqy.post.pojo.vo.MediasUploadUrlVO

interface PostsService {
	fun getMediasUploadUrl(dto: MediasUploadUrlDTO): MediasUploadUrlVO

	fun addPosts(dto: AddPostsDTO): AddPostsVO
}