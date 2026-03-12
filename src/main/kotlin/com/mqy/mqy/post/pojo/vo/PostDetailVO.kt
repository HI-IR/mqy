package com.mqy.mqy.post.pojo.vo

import com.mqy.mqy.database.entity.posts.MediaType

data class PostDetailVO(
	val postID: Long,
	val title: String,
	val content: String,
	val medias: List<DetailMedia>,
	val author: Author,
	val cat: Cat,
	val stats: PostStats,
	val createTime: String
)

data class Author(
	val userID: Long,
	val username: String,
	val avatarURL: String
)

data class Cat(
	val catID: Long,
	val name: String,
	val avatar: String
)

data class DetailMedia(
	val width: Int,
	val height: Int,
	val url: String,
	val mediaType: MediaType,
	val thumbnailURL: String
)

data class PostStats(
	val isLiked: Boolean,
	val likeCount: Long
)
