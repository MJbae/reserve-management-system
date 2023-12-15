package com.mj.usecase

import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.*
import com.mj.usecase.dto.PointEvent
import com.mj.usecase.exceptions.UseTransNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CancelPointAccountService(
    private val repository: PointAccountRepository,
    private val transRepository: PointTransactionRepository,
    private val kafkaTemplate: KafkaTemplate<String, PointEvent>,
    @Value("\${spring.kafka.topic.point.cancel}") private val pointCancelTopic: String,
) {

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun cancelPoint(memberId: String) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        val pointsUsed = findLatestUsedPoints(account)
        account.addPoints(pointsUsed)

        kafkaTemplate.send(pointCancelTopic, PointEvent(account.accountId.toString(), pointsUsed))
    }

    private fun findLatestUsedPoints(account: PointAccount): Int {
        val useTrans = transRepository.findAll(
            account.accountId,
            types = setOf(TransactionType.USE),
            pageReq = PageRequest.of(0, 1, Sort.by("createdAt").descending()),
        )

        if (useTrans.isEmpty) throw UseTransNotFoundException(account.accountId)

        return useTrans.first().points
    }
}
