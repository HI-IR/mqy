package com.mqy.mqy.common.koltinmapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper

/**
 * Kotlin语言下对LambdaWrapper的一些方法封装
 */
inline fun <reified T : Any> ktQuery() = KtQueryWrapper(T::class.java)
inline fun <reified T : Any> ktUpdate() = KtUpdateWrapper(T::class.java)