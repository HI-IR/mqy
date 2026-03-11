package com.mqy.mqy.cats.pojo.vo

import com.fasterxml.jackson.annotation.JsonProperty

data class CatDetailVO (
	@get:JsonProperty("arrival_date", required=true)@field:JsonProperty("arrival_date", required=true)
	val arrivalDate: String,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val avatar: String,

	@get:JsonProperty("birth_date", required=true)@field:JsonProperty("birth_date", required=true)
	val birthDate: String,

	@get:JsonProperty("cat_id", required=true)@field:JsonProperty("cat_id", required=true)
	val catID: Long,

	@get:JsonProperty("coat_color", required=true)@field:JsonProperty("coat_color", required=true)
	val coatColor: Int,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val gender: Int,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val name: String,

	@get:JsonProperty("neutered_status", required=true)@field:JsonProperty("neutered_status", required=true)
	val neuteredStatus: Int,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val position: String,

	@get:JsonProperty("user_info",required=true)@field:JsonProperty("user_info",required=true)
	val userInfo: UserStats,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val status: Int,

	@get:JsonProperty(required=true)@field:JsonProperty(required=true)
	val story: String
)

data class UserStats (
	@get:JsonProperty("is_following", required=true)@field:JsonProperty("is_following", required=true)
	val isFollowing: Boolean
)
