package com.mj.usecase

import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.*
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
) {
    private val transFactory = PointTransactionFactory()


    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun earnPoint(memberId: String, amount: Int) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val earnTrans = transFactory.createEarnTrans(accountId = pointAccount.accountId, amount = amount)
        transRepository.save(earnTrans)

        pointAccount.addPoints(earnTrans.points)
    }

    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun usePoint(memberId: String, amount: Int) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val useTrans = transFactory.createUseTrans(accountId = pointAccount.accountId, amount = amount)
        transRepository.save(useTrans)

        pointAccount.deductPoints(useTrans.points)
    }


    @Retryable(value = [ObjectOptimisticLockingFailureException::class, DataIntegrityViolationException::class])
    @Transactional
    fun cancelPoint(memberId: String) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val latestUseTrans = transRepository.findAll(
            pointAccount.accountId,
            types = setOf(TransactionType.USE),
            pageReq = PageRequest.of(0, 1, Sort.by("createdAt").descending()),
        )

        if (latestUseTrans.isEmpty) throw UseTransNotFoundException(pointAccount.accountId)

        val cancelTrans = transFactory.createCancelTrans(latestUseTrans.first())
        transRepository.save(cancelTrans)

        pointAccount.addPoints(cancelTrans.points)
    }
}
