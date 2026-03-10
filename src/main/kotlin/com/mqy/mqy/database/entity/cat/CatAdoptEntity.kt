package com.mqy.mqy.database.entity.cat

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

@TableName("cat_adopt")
class CatAdoptEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var userId: Long? = null
	var catId: Long? = null
	var contactInfo: String? = null
	var reason: String? = null
	var info: String? = null
	var isAccept: Int? = null
	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null
	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null
}