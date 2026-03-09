package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.*
import com.mqy.mqy.database.entity.posts.MediaType
import java.time.LocalDateTime

/**
 * post的媒体资源项
 */
@TableName("post_medias")
data class PostMediaEntity(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val postId: Long = 0L,
	val mediaType: MediaType = MediaType.IMAGE,
	val url: String = "",
	val thumbnailUrl: String = "",
	val width: Int = 0,
	val height: Int = 0,
	@TableField(fill = FieldFill.INSERT)
	val createTime: LocalDateTime? = null,
	@TableField(fill = FieldFill.INSERT_UPDATE)
	val updateTime: LocalDateTime? = null
)