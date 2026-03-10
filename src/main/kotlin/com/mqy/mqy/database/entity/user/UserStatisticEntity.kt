package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * 用户缩略信息表
 */
@TableName("user_statistics")
class UserStatisticEntity {
	@TableId(type = IdType.INPUT)
	var userId: Long? = null
	var totalLikesReceived: Long? = null
	var totalPostCount: Long? = null
	var totalFollowingCount: Long? = null
}