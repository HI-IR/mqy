package com.mqy.mqy.post.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.posts.PostEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

@Mapper
interface PostMapper : BaseMapper<PostEntity> {
	@Update("UPDATE user_statistics SET total_post_count = total_post_count + 1 WHERE user_id = #{userId}")
	fun increasePostCount(@Param("userId") userId: Long)
}