package com.mqy.mqy.auth.service.impl

import com.mqy.mqy.auth.mapper.AuthMapper
import com.mqy.mqy.auth.pojo.req.LoginReq
import com.mqy.mqy.auth.pojo.req.RegisterReq
import com.mqy.mqy.auth.pojo.vo.AvatarUploadVO
import com.mqy.mqy.auth.pojo.vo.LoginVO
import com.mqy.mqy.auth.service.AuthService
import com.mqy.mqy.auth.utlis.generateClaims
import com.mqy.mqy.common.utils.jwt.JwtUtils
import com.mqy.mqy.common.utils.upload.OssUtil
import com.mqy.mqy.common.utils.upload.StsProvider
import com.mqy.mqy.database.entity.user.UserEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class AuthServiceImpl(
	private val stsProvider: StsProvider,
	private val ossUtil: OssUtil,
	private val mapper: AuthMapper,
	private val authenticationManager: AuthenticationManager,
	private val jwtUtils: JwtUtils,
	private val passwordEncoder: PasswordEncoder
) : AuthService {
	// 获取头像预上传URL
	override fun getAvatarUploadUrl(): AvatarUploadVO {
		val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
		val uuid = UUID.randomUUID().toString().replace("-", "")
		val objectKey = "tmp/avatars/$dateStr-$uuid.jpg"
		//获取凭证
		val credentials = stsProvider.getCredentials(sessionName = "UserAvatarUpload")
		val presignedUrl = ossUtil.generatePresignedPutUrl(
			objectKey = objectKey,
			contentType = "image/jpeg",
			accessKeyId = credentials.accessKeyId,
			accessKeySecret = credentials.accessKeySecret,
			securityToken = credentials.securityToken
		)
		return AvatarUploadVO(
			uploadUrl = presignedUrl,
			objectKey = objectKey
		)
	}

	override fun doLoing(req: LoginReq): LoginVO {
		val authentication = authenticationManager.authenticate(
			UsernamePasswordAuthenticationToken(req.username, req.password)
		)
		val userDetails = authentication.principal as UserDetails
		val user = mapper.getUserByUsername(userDetails.username)?:throw RuntimeException("没有找到用户")

		val token = jwtUtils.generateToken(user.generateClaims())
		return LoginVO(
			userId = user.id.toString(),
			accessToken = token,
			expiresIn = 3600000
		)
	}

	/**
	 * 注册
	 */
	@Transactional
	override fun doRegister(request: RegisterReq): LoginVO {
		if (mapper.getUserByUsername(request.username) != null) {
			throw RuntimeException("用户名已存在")
		}

		val encodedPassword = passwordEncoder.encode(request.password)
		val sourceKey = request.avatarKey
		val targetKey = normalizeAvatarKey(sourceKey)

		try {
			ossUtil.moveObject(
				sourceKey = sourceKey,
				targetKey = targetKey,
			)

			val userEntity = UserEntity(
				id = null,
				username = request.username,
				passwordHash = encodedPassword,
				role = "USER",
				avatarUrl = ossUtil.getTheCompleteURL(targetKey)
			)

			mapper.insert(userEntity)

			val savedUser = mapper.getUserByUsername(request.username)
				?: throw RuntimeException("注册失败")

			val token = jwtUtils.generateToken(savedUser.generateClaims())

			return LoginVO(
				userId = savedUser.id.toString(),
				accessToken = token,
				expiresIn = 3600000
			)
		} catch (e: Exception) {
			throw RuntimeException("注册失败: ${e.message}", e)
		}
	}

	private fun normalizeAvatarKey(tempKey: String): String {
		require(tempKey.startsWith("tmp/avatars/")) { "非法头像路径" }
		return tempKey.removePrefix("tmp/")
	}
}