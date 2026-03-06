package com.mqy.mqy.common.utils.upload

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * 阿里云OSS服务的相关设置
 */
@ConfigurationProperties(prefix = "aliyun.oss")
data class AliyunProperties @ConstructorBinding constructor(
	val endpoint: String,
	val bucketName: String,  // Bucket名称
	val region: String // Bucket所在地域
)