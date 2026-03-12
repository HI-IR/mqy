package com.mqy.mqy.post.pojo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.mqy.mqy.cats.pojo.dto.mapper


data class MediasUploadUrlDTO(
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val files: List<MediasFileInfo>
) {
	fun toJson() = mapper.writeValueAsString(this)

	companion object {
		fun fromJson(json: String) = mapper.readValue<MediasUploadUrlDTO>(json)
	}
}

data class MediasFileInfo(
	@get:JsonProperty("content_type", required = true) @field:JsonProperty("content_type", required = true)
	val contentType: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val filename: String
)
