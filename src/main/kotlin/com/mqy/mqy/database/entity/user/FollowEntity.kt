package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

/**
 * 关注信息表
 */
@TableName("followings")
class FollowEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var userId: Long? = null
	var catId: Long? = null

	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null

	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null

	@TableLogic(value = "0", delval = "1")
	var deleted: Int = 0 // 0表示未删除，1表示已删除
}