package com.mqy.mqy.post.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.user.PostLikeEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface PostsLikeMapper : BaseMapper<PostLikeEntity> {
	@Select("SELECT * FROM post_like WHERE post_id = #{postId} AND user_id = #{userId}")
	fun findAnyStatusLikeRecord(@Param("postId") postId: Long, @Param("userId") userId: Long): PostLikeEntity?

	@Select("SELECT * FROM post_like WHERE post_id = #{postId} AND user_id = #{userId} AND deleted = 0")
	fun findLikedRecord(@Param("postId") postID: Long, @Param("userId") userId: Long): PostLikeEntity?


	// 用于恢复软删除的状态
	@Update("UPDATE post_like SET deleted = 0, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
	fun restoreLikeStatus(@Param("id") id: Long): Int


	@Update("UPDATE user_statistics SET total_likes_count = total_likes_count + 1 WHERE user_id = #{userId}")
	fun increaseLikeCount(@Param("userId") userId: Long)

	@Update("UPDATE user_statistics SET total_likes_count = total_likes_count - 1 WHERE user_id = #{userId} AND total_likes_count > 0")
	fun decreaseLikeCount(@Param("userId") userId: Long)

	@Update("UPDATE user_statistics SET total_likes_received = total_likes_received + 1 WHERE user_id = #{userId}")
	fun increaseLikeReceivedCount(@Param("userId") userId: Long)

	@Update("UPDATE user_statistics SET total_likes_received = total_likes_received - 1 WHERE user_id = #{userId} AND total_likes_received > 0")
	fun decreaseLikeReceivedCount(@Param("userId") userId: Long)
}