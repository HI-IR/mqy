package com.mqy.mqy.post.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.posts.PostMediaEntity
import org.apache.ibatis.annotations.Param

interface PostMediaMapper : BaseMapper<PostMediaEntity> {

	fun insertBatch(@Param("list") list: List<PostMediaEntity>): Int
}