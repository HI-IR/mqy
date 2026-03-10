package com.mqy.mqy.database.entity.posts

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName

@TableName("post_like_count")
class PostLikeCountEntity {
	@TableId(type = IdType.INPUT)
	var postId: Long? = null
	var postLikeCount: Long? = null

	@TableLogic(value = "0", delval = "1")
	var status: Int? = null // 0有效，1无效
}