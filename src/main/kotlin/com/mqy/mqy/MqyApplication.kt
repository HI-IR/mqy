package com.mqy.mqy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
@ConfigurationPropertiesScan("com.mqy")
@SpringBootApplication
class MqyApplication

fun main(args: Array<String>) {
	runApplication<MqyApplication>(*args)
}
