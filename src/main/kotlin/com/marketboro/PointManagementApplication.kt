package com.marketboro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class PointManagementApplication

fun main(args: Array<String>) {
    runApplication<PointManagementApplication>(*args)
}
