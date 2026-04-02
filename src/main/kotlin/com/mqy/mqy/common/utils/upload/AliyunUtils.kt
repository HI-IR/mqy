package com.mqy.mqy.common.utils.upload

import com.aliyun.oss.HttpMethod
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
			accessKeyId = System.getenv("OSS_ACCESS_KEY_ID")
			accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET")
			endpoint = "sts.${properties.region}.aliyuncs.com"
			println("AccessKeyId: ${accessKeyId}")
			println("Secret Length: ${accessKeySecret?.length}")
// 不要直接打印全称，打印长度即可对比。如果不符，说明读取环节出问题了。
		}
		Client(config)

	}

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
class OssUtil(
	private val properties: AliyunProperties
) {
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

	fun getTheCompleteURL(objectName: String): String =
		properties.endpoint.split("//".toRegex()).dropLastWhile { it.isEmpty() }
			.toTypedArray()[0] + "//" + properties.bucketName + "." + properties.endpoint.split("//".toRegex())
			.dropLastWhile { it.isEmpty() }
			.toTypedArray()[1] + "/" + objectName

	fun getVideoThumbnailUrl(originalVideoUrl: String, timeMs: Long = 0): String {
		val processParams = "?x-oss-process=video/snapshot,t_$timeMs,f_jpg,m_fast"
		return "$originalVideoUrl$processParams"
	}

	fun deleteFile(fileKey: String) {
        val ossClient = OSSClientBuilder().build(
            properties.endpoint,
            System.getenv("OSS_ACCESS_KEY_ID"),
            System.getenv("OSS_ACCESS_KEY_SECRET")
        )
        ossClient.deleteObject(properties.bucketName, fileKey)
	}
}