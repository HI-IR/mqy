package com.mqy.mqy.auth.service

import com.mqy.mqy.auth.pojo.req.LoginReq
import com.mqy.mqy.auth.pojo.req.RegisterReq
import com.mqy.mqy.auth.pojo.vo.AvatarUploadVO
import com.mqy.mqy.auth.pojo.vo.LoginVO
import org.springframework.security.core.userdetails.UserDetailsService

interface AuthService {
	fun getAvatarUploadUrl(): AvatarUploadVO

	fun doLoing(req: LoginReq): LoginVO

	fun doRegister(request: RegisterReq): LoginVO

}
