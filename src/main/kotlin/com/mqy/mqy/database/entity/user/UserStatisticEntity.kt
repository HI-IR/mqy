package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * 用户缩略信息表
 */
@TableName("user_statistics")
data class UserStatisticEntity(
	@TableId(type = IdType.INPUT)
	val userId: Int? = null,
	val totalLikesReceived: Long? = null,
	val totalPostCount: Long? = null,
	val totalFollowingCount: Long? = null
)