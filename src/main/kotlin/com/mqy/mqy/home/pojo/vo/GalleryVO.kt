package com.mqy.mqy.home.pojo.vo



data class GalleryVO (
	val cats: List<CatInfo>,
	val hasMore: Boolean,
	val nextCursor: Long
)

data class CatInfo (
	val avatar: String,
	val catID: Long,
	val coatColor: Int,
	val gender: Int,
	val name: String,
	val position: String,
	val state: Int
)
