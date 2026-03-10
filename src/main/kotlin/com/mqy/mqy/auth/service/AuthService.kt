package com.mqy.mqy.auth.service

import com.mqy.mqy.auth.pojo.req.LoginReq
import com.mqy.mqy.auth.pojo.req.RegisterReq
import com.mqy.mqy.auth.pojo.vo.UserAvatarUploadVO
import com.mqy.mqy.auth.pojo.vo.LoginVO

interface AuthService {
	fun getAvatarUploadUrl(): UserAvatarUploadVO

	fun doLoing(req: LoginReq): LoginVO

	fun doRegister(request: RegisterReq): LoginVO

}
