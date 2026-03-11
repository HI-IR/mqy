package com.mqy.mqy.post.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.posts.PostEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface PostMapper : BaseMapper<PostEntity> {
}