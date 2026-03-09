package com.mqy.mqy.common.utils.upload

import com.aliyun.oss.HttpMethod
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.GeneratePresignedUrlRequest
import com.aliyun.sts20150401.Client
import com.aliyun.sts20150401.models.AssumeRoleRequest
import com.aliyun.sts20150401.models.AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials
import com.aliyun.teaopenapi.models.Config
import com.aliyun.teautil.models.RuntimeOptions
import org.springframework.stereotype.Component
import java.util.*

/**
 * 用于获取STS临时授权
 */
@Component
class StsProvider(private val properties: AliyunProperties) {
	private val client: Client by lazy {
		val config = Config().apply {
			// 从系统变量中读取到accessKeyId和accessKeySecret
			accessKeyId = System.getenv("OSS_ACCESS_KEY_ID")
			accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET")
			endpoint = "sts.${properties.region}.aliyuncs.com"
		}
		Client(config)
	}

	/**
	 * 获取临时授权凭证
	 * @param sessionName 会话名称，用于审计区分
	 * @param duration 有效期（秒），默认 3600
	 */
	fun getCredentials(
		sessionName: String = "DefaultSession",
		duration: Long = 3600L
	): AssumeRoleResponseBodyCredentials {
		val request = AssumeRoleRequest().apply {
			roleArn = System.getenv("OSS_STS_ROLE_ARN")
			roleSessionName = sessionName
			durationSeconds = duration
		}
		val response = client.assumeRoleWithOptions(request, RuntimeOptions())
		return response.body.credentials
	}
}

@Component
class OssUtil(private val properties: AliyunProperties, private val ossClient: OSS) {
	/**
	 * 生成 PUT 方式的预签名 URL
	 *
	 * @param objectKey OSS 中的文件完整路径
	 * @param contentType 文件的 MIME 类型
	 * @param accessKeyId STS 提供的临时 AK
	 * @param accessKeySecret STS 提供的临时 SK
	 * @param securityToken STS 提供的 Token
	 * @param expireMinutes URL 有效期
	 */
	fun generatePresignedPutUrl(
		objectKey: String,
		contentType: String,
		accessKeyId: String,
		accessKeySecret: String,
		securityToken: String,
		expireMinutes: Int = 15
	): String {
		val ossClient = OSSClientBuilder().build(properties.endpoint, accessKeyId, accessKeySecret, securityToken)

		return try {
			val expiration = Date(System.currentTimeMillis() + expireMinutes * 60 * 1000L)
			val request = GeneratePresignedUrlRequest(properties.bucketName, objectKey, HttpMethod.PUT).apply {
				this.expiration = expiration
				this.contentType = contentType
			}
			ossClient.generatePresignedUrl(request).toString()
		} finally {
			ossClient.shutdown()
		}
	}

	/**
	 * 生成 PUT 方式的预签名 URL
	 * Params:
	 * sourceKey - 源路径
	 * targetKey - 目标路径
	 */
	fun moveObject(
		sourceKey: String,
		targetKey: String,
	) {
		println("${sourceKey}->${targetKey}")
		ossClient.copyObject(properties.bucketName, sourceKey, properties.bucketName, targetKey)
		ossClient.deleteObject(properties.bucketName, sourceKey)
	}

	/**
	 * 通过传入的objectName获取完整的URL
	 */
	fun getTheCompleteURL(objectName: String): String =
		properties.endpoint.split("//".toRegex()).dropLastWhile { it.isEmpty() }
			.toTypedArray()[0] + "//" + properties.bucketName + "." + properties.endpoint.split("//".toRegex())
			.dropLastWhile { it.isEmpty() }
			.toTypedArray()[1] + "/" + objectName

	/**
	 * 传入临时路径 转化为存储路径
	 * tmp/avatars/20260309-1350867f104f456a985860026ef8395b.jpg
	 * avatars/20260309-1350867f104f456a985860026ef8395b.jpg
	 */
	fun getTargetUrl(objectUrl: String): String = objectUrl.removePrefix("tmp/")
}