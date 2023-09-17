package com.marketboro.controller.helper

import java.util.*

class TestIdGenerator {
    fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}