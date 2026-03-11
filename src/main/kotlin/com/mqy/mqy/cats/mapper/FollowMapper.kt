package com.mqy.mqy.cats.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.user.FollowEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface FollowMapper : BaseMapper<FollowEntity> {
	@Update("UPDATE user_statistics SET total_following_count = total_following_count + 1 WHERE user_id = #{userId}")
	fun increaseFollowCatCount(@Param("userId") userId: Long)

	@Update("UPDATE user_statistics SET total_following_count = total_following_count - 1 WHERE user_id = #{userId} AND total_following_count > 0")
	fun decreaseFollowCatCount(@Param("userId") userId: Long)

	@Select("SELECT * FROM followings WHERE cat_id = #{catId} AND user_id = #{userId}")
	fun findAnyStatusFollowRecord(@Param("catId") catId: Long, @Param("userId") userId: Long): FollowEntity?

	// 用于恢复软删除的状态
	@Update("UPDATE followings SET status = 0, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
	fun restoreFollowStatus(@Param("id") id: Long): Int

	@Select("select * from followings where user_id = #{userId} and cat_id = #{catId} and status = 0 limit 1")
	fun findUserFollowCatByCatId(@Param("catId") catId: Long, @Param("userId") userId: Long): FollowEntity?

}