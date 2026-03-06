package com.mqy.mqy.common.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * 关于鉴权验证的相关参数
 */
@ConfigurationProperties(prefix = "auth.token")
data class AuthProperties @ConstructorBinding constructor(
	val secretKey: String,
	val ttl: Long = 3600000, // 默认 1 小时
	val tokenName: String
)