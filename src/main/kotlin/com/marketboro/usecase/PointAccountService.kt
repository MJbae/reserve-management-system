package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import com.marketboro.usecase.exceptions.InsufficientAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import com.marketboro.usecase.exceptions.UseTransNotFoundException
import org.springframework.data.domain.PageRequest
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
    fun loadHistory(memberId: String, pageNum: Int, pageSize: Int): PointHistoryDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val transactionsPage = transRepository.findByLatest(pointAccount.accountId,
            types = setOf(TransactionType.USE, TransactionType.EARN), pageReq = PageRequest.of(pageNum, pageSize))
        return PointHistoryDto(transactionsPage)
    }

    @Transactional
    fun cancelPoint(memberId: String) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val latestUseTrans = transRepository.findByLatest(pointAccount.accountId,
            types = setOf(TransactionType.USE), pageReq = PageRequest.of(0, 1))
        if (latestUseTrans.isEmpty) throw UseTransNotFoundException(pointAccount.accountId)

        val cancelTrans = transFactory.createCancelTrans(latestUseTrans.first())
        transRepository.save(cancelTrans)

        pointAccount.sumPoints(cancelTrans.points)
    }

}