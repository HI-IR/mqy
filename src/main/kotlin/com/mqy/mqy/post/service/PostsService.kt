package com.mqy.mqy.post.service

import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.pojo.vo.AddPostsVO
import com.mqy.mqy.post.pojo.vo.MediasUploadUrlVO
import com.mqy.mqy.post.pojo.vo.PostDetailVO
import com.mqy.mqy.post.pojo.vo.PostsSearchCatVO

interface PostsService {
	fun getMediasUploadUrl(dto: MediasUploadUrlDTO): MediasUploadUrlVO

	fun addPosts(dto: AddPostsDTO): AddPostsVO

	suspend fun getPostDetail(userId: Long, postId: Long): PostDetailVO

	fun postsSearchCat(keyword: String, limit: Long): List<PostsSearchCatVO>

	fun likePost(id: Long)

	fun unLikePost(id: Long)

	fun deletePost(postId: Long)
}