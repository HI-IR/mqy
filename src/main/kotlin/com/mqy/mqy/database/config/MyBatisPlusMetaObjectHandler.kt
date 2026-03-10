package com.mqy.mqy.database.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * MybatisPlus的元对象处理器
 */
@Component
class MyBatisPlusMetaObjectHandler : MetaObjectHandler {
	override fun insertFill(metaObject: MetaObject?) {
		// 在插入时自动填充的内容
		this.strictInsertFill(metaObject, "createTime", LocalDateTime::class.java, LocalDateTime.now())
		this.strictInsertFill(metaObject, "updateTime", LocalDateTime::class.java, LocalDateTime.now())
	}

	override fun updateFill(metaObject: MetaObject?) {
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::class.java, LocalDateTime.now())
	}
}