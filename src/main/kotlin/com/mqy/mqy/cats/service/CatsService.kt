package com.mqy.mqy.cats.service

import com.mqy.mqy.cats.pojo.dto.AddCatsDTO
import com.mqy.mqy.cats.pojo.dto.AdoptCatDTO
import com.mqy.mqy.cats.pojo.vo.CatDetailVO
import com.mqy.mqy.cats.pojo.vo.CatsAddVO
import com.mqy.mqy.cats.pojo.vo.CatsAvatarUploadVO

interface CatsService {
	fun getAvatarUploadUrl(): CatsAvatarUploadVO
	fun addCats(addCatsDTO: AddCatsDTO): CatsAddVO
	fun followCat(id: Long)
	fun unfollowCat(id: Long)
	fun getCatDetail(id: Long):CatDetailVO
	fun adoptCat(adoptCatDTO: AdoptCatDTO)
}