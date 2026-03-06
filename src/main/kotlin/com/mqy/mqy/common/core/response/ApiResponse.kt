package com.mqy.mqy.common.core.response

data class ApiResponse(
	//code 为 0 表示error
	//code 为 1 表示success
	val code: Int,
	val msg: String,
	val data: Any?
) {
	companion object {
		fun success(): ApiResponse = ApiResponse(1, "success", null)
		fun success(data: Any): ApiResponse = ApiResponse(1, "success", data)
		fun error(message: String): ApiResponse = ApiResponse(0, msg = message, null)
	}
}