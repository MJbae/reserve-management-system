package com.mj.usecase

import com.mj.controller.PointEventListener
import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.*
import com.mj.usecase.dto.PointEvent
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val eventListener: PointEventListener
) {

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun earnPoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        account.addPoints(amount)

        eventListener.onPointEarned(PointEvent(account.accountId, amount))
    }

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun usePoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        account.deductPoints(amount)

        eventListener.onPointUsed(PointEvent(account.accountId, amount))
    }
}
