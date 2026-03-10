package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

//   帖子的实体类
@TableName("post")
data class Post(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val userId: Long = 0L,
	val catId: Long = 0L,
	val title: String? = null,
	val content: String? = null,
	@TableField(fill = FieldFill.INSERT)
	val createTime: LocalDateTime? = null,
	@TableField(fill = FieldFill.INSERT_UPDATE)
	val updateTime: LocalDateTime? = null
)