package com.mj.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.TableGenerator

@Entity
class PointEvent(
    val accountId: String,
    val amount: Int
) {
    @Id
    @TableGenerator(name = "PointEventIdGenerator", table = "sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PointEventIdGenerator")
    var id: Long? = null
}