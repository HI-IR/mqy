package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("users")
data class UserEntity(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val username: String = "",
	val passwordHash: String = "",
	val avatarUrl: String = "",
	val role: String = "USER"
)