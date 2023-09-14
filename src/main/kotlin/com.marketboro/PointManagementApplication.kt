package com.marketboro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PointManagementApplication

fun main(args: Array<String>) {
    runApplication<PointManagementApplication>(*args)
}
