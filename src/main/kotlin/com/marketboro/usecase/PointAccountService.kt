package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import com.marketboro.usecase.dto.TransactionDto
import com.marketboro.usecase.exceptions.InsufficientAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import com.marketboro.usecase.exceptions.UseTransNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val transRepository: PointTransactionRepository
) {
    private val transFactory = PointTransactionFactory()

    @Transactional(readOnly = true)
    fun getTotalPoints(memberId: String): TotalPointsDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        return TotalPointsDto(totalPoints = pointAccount.totalPoints())
    }

    @Transactional
    fun earnPoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val earnTrans = transFactory.createEarnTrans(accountId = pointAccount.accountId, amount = points)
        transRepository.save(earnTrans)

        pointAccount.sumPoints(earnTrans.points)
    }

    @Transactional
    fun usePoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        if (!pointAccount.canDeduct(points)) {
            throw InsufficientAmountException(pointAccount.totalPoints())
        }

        val useTrans = transFactory.createUseTrans(accountId = pointAccount.accountId, amount = -points)
        transRepository.save(useTrans)

        pointAccount.sumPoints(useTrans.points)
    }

    @Transactional(readOnly = true)
    fun loadHistory(memberId: String): PointHistoryDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        val transactions = transRepository.findAll(pointAccount.accountId)

        return PointHistoryDto(transactions.map { TransactionDto(type = it.type, amount = it.points) })
    }

    @Transactional
    fun cancelPoint(memberId: String) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        val latestUseTrans = transRepository.findLatestUseTrans(pointAccount.accountId)
            ?: throw UseTransNotFoundException(pointAccount.accountId)
        val cancelTrans = transFactory.createCancelTrans(latestUseTrans)
        transRepository.save(cancelTrans)

        pointAccount.sumPoints(cancelTrans.points)
    }

}