package com.mqy.mqy.auth.service

import com.mqy.mqy.auth.pojo.AvatarUploadVO

interface AuthService {
	fun getAvatarUploadUrl(): AvatarUploadVO
}
