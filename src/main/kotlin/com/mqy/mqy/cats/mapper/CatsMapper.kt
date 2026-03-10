package com.mqy.mqy.cats.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.cat.CatEntity
import com.mqy.mqy.database.entity.user.UserEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CatsMapper : BaseMapper<CatEntity> {
	@Select("select * from users where id = #{id} LIMIT 1")
	fun selectUserByUserId(@Param("id")id: Long): UserEntity?

	@Select("select * from cat where id = #{id} LIMIT 1")
	fun selectCatByCatId(@Param("id")id: Long): CatEntity?
}