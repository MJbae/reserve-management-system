package com.mj.controller

import com.mj.domain.AccountId
import com.mj.usecase.PointTransactionService
import com.mj.usecase.dto.PointEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class PointEventListener(
    private val service: PointTransactionService
) {

    @KafkaListener(
        topics = ["\${spring.kafka.topic.point.earn}"],
        groupId = "point-management-group",
        containerFactory = "pointListenerContainerFactory"
    )
    fun onPointEarned(event: PointEvent) {
        service.earn(AccountId(event.accountId), event.amount)
    }

    @KafkaListener(
        topics = ["\${spring.kafka.topic.point.use}"],
        groupId = "point-management-group",
        containerFactory = "pointListenerContainerFactory"
    )
    fun onPointUsed(event: PointEvent) {
        service.use(AccountId(event.accountId), event.amount)
    }

    @KafkaListener(
        topics = ["\${spring.kafka.topic.point.cancel}"],
        groupId = "point-management-group",
        containerFactory = "pointListenerContainerFactory"
    )
    fun onPointCancelled(event: PointEvent) {
        service.cancel(AccountId(event.accountId), event.amount)
    }
}
