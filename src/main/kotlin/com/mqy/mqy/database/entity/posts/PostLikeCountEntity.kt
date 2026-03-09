package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("post_like_count")
data class PostLikeCountEntity(
	@TableId(type = IdType.INPUT)
	val postId: Long? = null,
	val postLikeCount: Long? = null,
	val status: Int? = null // 0无效，1有效
)