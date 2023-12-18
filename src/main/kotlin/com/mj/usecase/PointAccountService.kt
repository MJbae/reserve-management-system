package com.mj.usecase

import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val kafkaTemplate: KafkaTemplate<String, PointEvent>,
    @Value("\${spring.kafka.topic.point.earn}") private val pointEarnTopic: String,
    @Value("\${spring.kafka.topic.point.use}") private val pointUseTopic: String,
) {

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun earnPoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        account.addPoints(amount)

        kafkaTemplate.send(pointEarnTopic, PointEvent(account.accountId.toString(), amount))
    }

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun usePoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        account.deductPoints(amount)

        kafkaTemplate.send(pointUseTopic, PointEvent(account.accountId.toString(), amount))
    }
}
