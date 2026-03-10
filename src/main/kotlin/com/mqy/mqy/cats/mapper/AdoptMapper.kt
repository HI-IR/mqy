package com.mqy.mqy.cats.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mqy.mqy.database.entity.cat.CatAdoptEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AdoptMapper: BaseMapper<CatAdoptEntity> {
}