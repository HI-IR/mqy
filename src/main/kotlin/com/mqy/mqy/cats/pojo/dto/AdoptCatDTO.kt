package com.mqy.mqy.cats.pojo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue

data class AdoptCatDTO(
	@get:JsonProperty("cat_id", required = true) @field:JsonProperty("cat_id", required = true)
	val catID: Long,

	@get:JsonProperty("contact_info", required = true) @field:JsonProperty("contact_info", required = true)
	val contactInfo: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val info: String,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val reason: String
) {
	fun toJson() = mapper.writeValueAsString(this)

	companion object {
		fun fromJson(json: String) = mapper.readValue<AdoptCatDTO>(json)
	}
}
