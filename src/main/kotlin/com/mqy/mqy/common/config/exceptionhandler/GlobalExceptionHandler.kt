package com.mqy.mqy.common.config.exceptionhandler

import com.mqy.mqy.common.core.response.ApiResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
	@ExceptionHandler
	fun handleException(e: Exception) = ApiResponse.error(e.message!!)
}