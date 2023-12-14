package com.mj.controller

import com.mj.usecase.PointTransactionService
import com.mj.usecase.dto.PointCancelledEvent
import com.mj.usecase.dto.PointEarnedEvent
import com.mj.usecase.dto.PointUsedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PointEventListener(
    private val service: PointTransactionService
) {

    @EventListener
    fun onPointEarned(event: PointEarnedEvent) {
        service.earn(event.accountId, event.amount)
    }

    @EventListener
    fun onPointUsed(event: PointUsedEvent) {
        service.use(event.accountId, event.amount)
    }

    @EventListener
    fun onPointCancelled(event: PointCancelledEvent) {
        service.cancel(event.accountId, event.amount)
    }
}