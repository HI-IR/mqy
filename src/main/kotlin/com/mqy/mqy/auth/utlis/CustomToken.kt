package com.mqy.mqy.auth.utlis

import com.mqy.mqy.database.entity.user.UserEntity

/**
 * 提供定制的Token
 */
fun UserEntity.generateClaims() = mapOf(
	"id" to id.toString(),
	"username" to username!!,
	"avatarUrl" to avatarUrl!!,
	"role" to role!!
)