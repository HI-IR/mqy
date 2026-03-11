package com.mqy.mqy.post.pojo.vo


data class MediasUploadUrlVO(
	val credentials: List<Credential>
)

data class Credential(
	val filename: String,
	val objectKey: String,
	val uploadURL: String
)