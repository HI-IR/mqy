package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

//   帖子的实体类
@TableName("post")
class PostEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var userId: Long? = null
	var catId: Long? = null
	var title: String? = null
	var content: String? = null

	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null
	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null
}