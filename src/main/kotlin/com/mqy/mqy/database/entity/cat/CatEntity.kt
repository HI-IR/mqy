package com.mqy.mqy.database.entity.cat

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDate
import java.time.LocalDateTime

@TableName("cat")
data class CatEntity(
	@TableId(type = IdType.AUTO)
	val id: Long? = null,
	val name: String = "",
	val avatar: String? = null,
	val gender: Int = 0,
	val neuteredStatus: Int = 0,
	val birthDate: LocalDate = LocalDate.now(),
	val arrivalDate: LocalDate = LocalDate.now(),
	val status: Int = 0,
	val coatColor: Int = 0,
	val position: String = "",
	val story: String? = null,
	val creatorId: Long = 0L,

	@TableField(fill = FieldFill.INSERT)
	val createTime: LocalDateTime = LocalDateTime.now(),

	@TableField(fill = FieldFill.INSERT_UPDATE)
	val updateTime: LocalDateTime = LocalDateTime.now()
)