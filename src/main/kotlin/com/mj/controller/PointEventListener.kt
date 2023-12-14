package com.mj.controller

import com.mj.usecase.PointTransactionService
import com.mj.usecase.dto.PointEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PointEventListener(
    private val service: PointTransactionService
) {

    @EventListener
    fun onPointEarned(event: PointEvent) {
        service.earn(event.accountId, event.amount)
    }

    @EventListener
    fun onPointUsed(event: PointEvent) {
        service.use(event.accountId, event.amount)
    }

    @EventListener
    fun onPointCancelled(event: PointEvent) {
        service.cancel(event.accountId, event.amount)
    }
}