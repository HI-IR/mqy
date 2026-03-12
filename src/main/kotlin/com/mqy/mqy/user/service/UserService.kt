package com.mqy.mqy.user.service

import com.mqy.mqy.user.vo.UserInfoVO

interface UserInfoService {
	fun getUserInfo(): UserInfoVO
}