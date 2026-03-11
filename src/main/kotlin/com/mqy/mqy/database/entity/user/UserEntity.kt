package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("users")
class UserEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var username: String? = null
	var passwordHash: String? = null
	var avatarUrl: String? = null
	var role: String? = null
}