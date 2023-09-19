package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.exceptions.InsufficientAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import com.marketboro.usecase.exceptions.UseTransNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val transRepository: PointTransactionRepository,
) {
    private val transFactory = PointTransactionFactory()

    @Transactional
    fun earnPoint(memberId: String, amount: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val earnTrans = transFactory.createEarnTrans(accountId = pointAccount.accountId, amount = amount)
        transRepository.save(earnTrans)

        pointAccount.sumPoints(earnTrans.points)
    }

    @Transactional
    fun usePoint(memberId: String, amount: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        if (!pointAccount.canDeduct(amount)) {
            throw InsufficientAmountException(pointAccount.totalPoints())
        }

        val useTrans = transFactory.createUseTrans(accountId = pointAccount.accountId, amount = -amount)
        transRepository.save(useTrans)

        pointAccount.sumPoints(useTrans.points)
    }

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

        pointAccount.sumPoints(cancelTrans.points)
    }
}
