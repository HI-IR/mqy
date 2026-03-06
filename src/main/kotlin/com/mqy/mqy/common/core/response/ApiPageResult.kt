package com.mqy.mqy.common.core.response

/**
 * 分页查询的返回格式
 */
data class APIPageResult<T>(
	val total: Long,
	val rows: List<T>
)