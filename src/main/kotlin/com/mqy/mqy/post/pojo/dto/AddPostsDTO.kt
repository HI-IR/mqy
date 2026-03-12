package com.mqy.mqy.post.pojo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.mqy.mqy.cats.pojo.dto.mapper
import com.mqy.mqy.database.entity.posts.MediaType

data class AddPostsDTO(
	@get:JsonProperty("cat_id", required = true) @field:JsonProperty("cat_id", required = true)
	val catId: Long,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val title: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val content: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val medias: List<Media>
) {
	fun toJson() = mapper.writeValueAsString(this)

	companion object {
		fun fromJson(json: String) = mapper.readValue<AddPostsDTO>(json)
	}
}

data class Media(
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val height: Int,
	/**
	 * 或者VIDEO 、IMAGE
	 */
	@get:JsonProperty("media_type", required = true)
	@field:JsonProperty("media_type", required = true)
	val mediaType: MediaType,

	@get:JsonProperty("object_key", required = true) @field:JsonProperty("object_key", required = true)
	val objectKey: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val width: Int
)
