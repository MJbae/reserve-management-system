package com.mj.controller

import com.mj.usecase.MessagePublishingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class MessagePublishingJob(
    private val service: MessagePublishingService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 1000)
    fun publishMessages() {
        logger.info("message publishing task is scheduled")
        service.publish()
    }
}
