package com.mqy.mqy.cats.pojo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate

val mapper = jacksonObjectMapper().apply {
	propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
	setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

data class AddCatsDTO(
	@get:JsonProperty("arrival_date", required = true)
	@field:JsonProperty("arrival_date", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	val arrivalDate: LocalDate,

	/**
	 * tmp/avatars/20260304-uuid.jpg
	 */
	@get:JsonProperty("avatar_key", required = true) @field:JsonProperty("avatar_key", required = true)
	val avatarKey: String,

	/**
	 * 2023-05-01
	 */
	@get:JsonProperty("birth_data", required = true)
	@field:JsonProperty("birth_data", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	val birthData: LocalDate,

	/**
	 * 1-橘猫/橘白，2-玳瑁/三花，3-纯色，4-奶牛，5-狸花/狸白，6-雀猫，7-其他
	 */
	@get:JsonProperty("coat_color", required = true) @field:JsonProperty("coat_color", required = true)
	val coatColor: Int,

	/**
	 * 0 - 妹妹，1 - 弟弟
	 */
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val gender: Int,

	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val name: String,

	/**
	 * 0-未知，1-已绝育，2-未绝育
	 */
	@get:JsonProperty("neutered_status", required = true) @field:JsonProperty("neutered_status", required = true)
	val neuteredStatus: Int,

	/**
	 * 重庆市渝中区...
	 */
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val position: String,

	/**
	 * 1-待领养，2-已被领养，3-在家，4-失踪，5-去了喵星
	 */
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val status: Int,

	/**
	 * 那是一个晴朗的晚上.............
	 */
	@get:JsonProperty(required = true) @field:JsonProperty(required = true)
	val story: String
) {
	fun toJson() = mapper.writeValueAsString(this)

	companion object {
		fun fromJson(json: String) = mapper.readValue<AddCatsDTO>(json)
	}
}