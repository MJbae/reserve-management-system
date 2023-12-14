package com.mj.usecase

import com.mj.controller.PointEventListener
import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.*
import com.mj.usecase.dto.PointEvent
import com.mj.usecase.exceptions.UseTransNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val transRepository: PointTransactionRepository,
    private val eventListener: PointEventListener
) {
    private val transFactory = PointTransactionFactory()


    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun earnPoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val earnTrans = transFactory.createEarnTrans(accountId = account.accountId, amount = amount)
        transRepository.save(earnTrans)

        account.addPoints(earnTrans.points)

        eventListener.onPointEarned(PointEvent(account.accountId, earnTrans.points))
    }

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun usePoint(memberId: String, amount: Int) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val useTrans = transFactory.createUseTrans(accountId = account.accountId, amount = amount)
        transRepository.save(useTrans)

        account.deductPoints(useTrans.points)

        eventListener.onPointUsed(PointEvent(account.accountId, useTrans.points))
    }


    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun cancelPoint(memberId: String) {
        val account = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val useTrans = transRepository.findAll(
            account.accountId,
            types = setOf(TransactionType.USE),
            pageReq = PageRequest.of(0, 1, Sort.by("createdAt").descending()),
        )

        if (useTrans.isEmpty) throw UseTransNotFoundException(account.accountId)

        account.addPoints(useTrans.first().points)

        eventListener.onPointCancelled(PointEvent(account.accountId, useTrans.first().points))
    }
}
