package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 关注信息表
 */
@TableName("followings")
data class FollowEntity(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val userId: Long = 0L,
	val catId: Long = 0L,
	val status: Int = 1, // 0表示取消关注，1表示关注
	@TableField(fill = FieldFill.INSERT)
	val createTime: LocalDateTime? = null,
	@TableField(fill = FieldFill.INSERT_UPDATE)
	val updateTime: LocalDateTime? = null
)