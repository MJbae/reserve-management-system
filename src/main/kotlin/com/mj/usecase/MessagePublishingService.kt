package com.mj.usecase

import com.mj.domain.PointEvent
import com.mj.domain.PointEventOutboxRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessagePublishingService(
    private val outbox: PointEventOutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, PointEvent>,
    @Value("\${spring.kafka.topic.point.cancel}") private val pointCancelTopic: String,
) {
    @Transactional
    fun publish() {
        val messages = outbox.findAll()
        messages.forEach { kafkaTemplate.send(pointCancelTopic, it) }
        outbox.delete(messages.map { it.id })
    }
}