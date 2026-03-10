package com.mqy.mqy.database.entity.cat

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDate
import java.time.LocalDateTime

@TableName("cat")
class CatEntity {
	@TableId(type = IdType.AUTO)
	var id: Long? = null
	var name: String? = null
	var avatar: String? = null
	var gender: Int? = null
	var neuteredStatus: Int? = null
	var birthDate: LocalDate? = null
	var arrivalDate: LocalDate? = null
	var status: Int? = null
	var coatColor: Int? = null
	var position: String? = null
	var story: String? = null
	var creatorId: Long? = null

	@TableField(fill = FieldFill.INSERT)
	var createTime: LocalDateTime? = null

	@TableField(fill = FieldFill.INSERT_UPDATE)
	var updateTime: LocalDateTime? = null
}