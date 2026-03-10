package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

/**
 * post的媒体资源项
 */
@TableName("post_medias")
class PostMediaEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var postId: Long? = null
	var mediaType: MediaType = MediaType.IMAGE
	var url: String? = null
	var thumbnailUrl: String? = null
	var width: Int? = null
	var height: Int? = null

	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null

	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null
}