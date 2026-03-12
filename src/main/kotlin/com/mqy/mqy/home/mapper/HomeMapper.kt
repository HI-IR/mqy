package com.mqy.mqy.home.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.cat.CatEntity
import com.mqy.mqy.database.entity.posts.PostEntity
import com.mqy.mqy.database.entity.posts.PostMediaEntity
import com.mqy.mqy.database.entity.user.PostLikeEntity
import com.mqy.mqy.database.entity.user.UserEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HomePostsMapper : BaseMapper<PostEntity>

@Mapper
interface HomeUserMapper : BaseMapper<UserEntity>

@Mapper
interface HomeCatMapper : BaseMapper<CatEntity>

@Mapper
interface HomeMediaMapper : BaseMapper<PostMediaEntity>

@Mapper
interface HomePostLikeMapper : BaseMapper<PostLikeEntity>