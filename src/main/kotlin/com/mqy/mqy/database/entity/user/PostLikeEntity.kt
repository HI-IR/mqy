package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

//动态点赞
@TableName("post_like")
class PostLikeEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var userId: Long? = null
	var postId: Long? = null

	@TableLogic(value = "0", delval = "1")
	var state: Int? = null // 0表示有效， 表示无效

	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null
	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null
}