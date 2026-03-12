package com.mqy.mqy.home.pojo.vo

import com.mqy.mqy.database.entity.posts.MediaType

data class GetPostsVO (
	val hasMore: Boolean,
	val nextCursor: Long,
	val posts: List<PostItem>
)

data class PostItem (
	val cat: CatItem,
	val content: String,
	val createTime: String,
	val interaction: Interaction,
	val medias: List<MediaItem>,
	val postID: Long,
	val publisher: Publisher,
	val title: String
)

data class CatItem (
	val avatar: String,
	val catID: Long,
	val name: String
)

data class Interaction (
	val isLiked: Boolean,
	val likeCount: Long
)

data class MediaItem (
	val height: Int,
	val mediaType: MediaType,
	val thumbnailURL: String,
	val url: String,
	val width: Int
)

data class Publisher (
	val avatarURL: String,
	val userID: Long,
	val username: String
)
