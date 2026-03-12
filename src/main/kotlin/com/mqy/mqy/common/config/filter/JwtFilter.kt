package com.mqy.mqy.common.config.filter

import com.mqy.mqy.common.utils.jwt.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

/**
 * JWT鉴权验证的拦截器(只标注是否通过鉴权，拦截的功能由Spring Security 会自动拦截)
 */
@Component
class JwtFilter(private val jwtUtils: JwtUtils, private val handlerExceptionResolver: HandlerExceptionResolver) :
	OncePerRequestFilter() {

	// 允许拦截异步转发，让其重新组装一下SecurityContext
	override fun shouldNotFilterAsyncDispatch(): Boolean {
		return false
	}

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val header: String? = request.getHeader("Authorization")
		if (header != null && header.startsWith("Bearer ")) {
			val token = header.substring(7)
			try {
				val map = jwtUtils.parseToken(token)
				val username = map["username"] ?: ""
				val role = map["role"] as String
				val id = map["id"] ?: ""
				val userDetails = CustomUserDetail(id as String, username as String)
				val authorities = listOf(SimpleGrantedAuthority(role))
				val authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities).apply {
					details = WebAuthenticationDetailsSource().buildDetails(request)
				}
				SecurityContextHolder.getContext().authentication = authentication
			} catch (e: Exception) {
				SecurityContextHolder.clearContext()
				// 把异常交给一场拦截器处理
				handlerExceptionResolver.resolveException(request, response, null, e)
				return
			}
		}


		// 继续转发通知
		filterChain.doFilter(request, response)
	}
}