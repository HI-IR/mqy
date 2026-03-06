package com.mqy.mqy.auth.service.impl

import com.mqy.mqy.auth.pojo.AvatarUploadVO
import com.mqy.mqy.auth.service.AuthService
import com.mqy.mqy.common.utils.upload.OssSignUtil
import com.mqy.mqy.common.utils.upload.StsProvider
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class AuthServiceImpl(private val stsProvider: StsProvider, private val ossSignUtil: OssSignUtil) : AuthService {
	// 获取头像预上传URL
	override fun getAvatarUploadUrl(): AvatarUploadVO {
		val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
		val uuid = UUID.randomUUID().toString().replace("-", "")
		val objectKey = "tmp/avatars/$dateStr-$uuid.jpg"
		//获取凭证
		val credentials = stsProvider.getCredentials(sessionName = "UserAvatarUpload")
		val presignedUrl = ossSignUtil.generatePresignedPutUrl(
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
}