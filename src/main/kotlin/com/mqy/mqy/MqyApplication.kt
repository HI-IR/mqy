package com.mqy.mqy

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@ConfigurationPropertiesScan("com.mqy")
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.mqy.mqy.**.mapper")
class MqyApplication

fun main(args: Array<String>) {
	runApplication<MqyApplication>(*args)
}
