package com.mqy.mqy.common.core.response

data class ApiResponse(
	//code 为 400 表示error
	//code 为 200 表示success
	val code: Int,
	val msg: String,
	val data: Any?
) {
	companion object {
		fun success(): ApiResponse = ApiResponse(200, "success", null)
		fun success(data: Any): ApiResponse = ApiResponse(200, "success", data)
		fun error(message: String): ApiResponse = ApiResponse(400, msg = message, null)
	}
}
/*
{
	"code" : 200,
	"msg" : "success",
	"data" :
}
 */