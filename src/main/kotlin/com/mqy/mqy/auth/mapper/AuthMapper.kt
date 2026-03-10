package com.mqy.mqy.auth.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.user.UserEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface AuthMapper : BaseMapper<UserEntity> {
	@Select("SELECT * FROM users WHERE username = #{username} LIMIT 1")
	fun getUserByUsername(@Param("username") username: String): UserEntity?
}