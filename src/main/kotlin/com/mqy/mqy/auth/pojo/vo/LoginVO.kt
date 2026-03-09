package com.mqy.mqy.auth.pojo.vo

data class LoginVO(
	val userId: String,
	val accessToken: String,
	val expiresIn: Long
)