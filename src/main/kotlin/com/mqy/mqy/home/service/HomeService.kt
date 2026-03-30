package com.mqy.mqy.home.service

import com.mqy.mqy.home.pojo.vo.GalleryVO
import com.mqy.mqy.home.pojo.vo.GetPostsVO

interface HomeService {
	suspend fun getHomePosts(userId: Long, catId: Long?, cursor: Long?, limit: Int, keyword: String?): GetPostsVO
	suspend fun getGallery(cursor: Long?, limit: Int, state: Int?, keyword: String?): GalleryVO
}