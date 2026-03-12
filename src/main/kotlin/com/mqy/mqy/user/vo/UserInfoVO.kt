package com.mqy.mqy.user.vo

data class UserInfoVO(
	val avatarURL: String,
	val name: String,
	val userFollowCount: Long,
	val userID: Long,
	val userLikeCount: Long,
	val userLikeReceived: Long,
	val userPostCount: Long
)
