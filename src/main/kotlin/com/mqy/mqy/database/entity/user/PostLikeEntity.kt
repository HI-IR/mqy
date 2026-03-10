package com.mqy.mqy.database.entity.user

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

//动态点赞
@TableName("post_like")
data class PostLikeEntity(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val userId: Long = 0L,
	val postId: Long = 0L,
	val state: Int = 1, // 1表示动态点赞，0表示取消点赞

	@TableField(fill = FieldFill.INSERT)
	val createTime: LocalDateTime? = null,

	@TableField(fill = FieldFill.INSERT_UPDATE)
	val updateTime: LocalDateTime? = null
)