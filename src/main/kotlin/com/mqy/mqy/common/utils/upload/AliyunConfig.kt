package com.mqy.mqy.common.utils.upload

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 阿里云OSS服务的相关设置
 */
@ConfigurationProperties(prefix = "aliyun.oss")
data class AliyunProperties @ConstructorBinding constructor(
	val endpoint: String,
	val bucketName: String,  // Bucket名称
	val region: String // Bucket所在地域
)

@Configuration
@EnableConfigurationProperties(AliyunProperties::class)
class OssConfig(
	private val properties: AliyunProperties
) {
	@Bean
	fun ossClient(): OSS {
		val accessKeyId = System.getenv("OSS_ACCESS_KEY_ID")
		val accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET")
		return OSSClientBuilder().build(properties.endpoint, accessKeyId, accessKeySecret)
	}
}