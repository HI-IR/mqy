package com.mqy.mqy.common.config.filter

/**
 * 自定义的UserDetail，用于在Spring SecurityContext中保存
 */
data class CustomUserDetail(
	val id: String,
	val username: String
)