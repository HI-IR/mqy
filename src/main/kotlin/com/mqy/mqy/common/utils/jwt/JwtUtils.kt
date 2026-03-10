package com.mqy.mqy.common.utils.jwt

import com.mqy.mqy.common.config.properties.AuthProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils(properties: AuthProperties) {
	private val secretKey: SecretKey = Keys.hmacShaKeyFor(properties.secretKey.toByteArray())
	private val expirationMs: Long = properties.ttl

	// 根据claims生成加密后的Token
	fun generateToken(claims: Map<String, Any>): String {
		val now = Date()
		val expiryDate = Date(now.time + expirationMs)
		return Jwts.builder()
			.claims(claims)
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(secretKey)
			.compact()
	}

	/**
	 * 解析token，返回claims，如果解析失败(Token被篡改)直接抛出异常
	 */
	fun parseToken(token: String): Map<String, Any> {
		return try{
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload
		} catch (e: Exception) {
			throw AuthException("鉴权解析错误: ${e.message}")
		}
	}
}