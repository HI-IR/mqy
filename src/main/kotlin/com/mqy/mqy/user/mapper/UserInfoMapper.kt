package com.mqy.mqy.user.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.user.UserEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserInfoMapper : BaseMapper<UserEntity>

