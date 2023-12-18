package com.mj.domain


interface PointEventOutboxRepository {
    fun findAll(): List<PointEvent>
    fun delete(ids: List<Long?>)
    fun save(pointEvent: PointEvent)
}
