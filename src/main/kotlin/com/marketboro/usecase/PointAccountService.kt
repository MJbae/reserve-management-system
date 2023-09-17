package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import com.marketboro.usecase.dto.TransactionDto
import com.marketboro.usecase.exceptions.InsufficientAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository,
    private val transactionRepository: PointTransactionRepository
) {
    private val factory = PointTransactionFactory()

    @Transactional(readOnly = true)
    fun getTotalPoints(memberId: String): TotalPointsDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        return TotalPointsDto(totalPoints = pointAccount.totalPoints())
    }

    @Transactional
    fun earnPoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        val newTransaction = factory.createEarnTransaction(accountId = pointAccount.accountId, amount = points)
        transactionRepository.save(newTransaction)
        pointAccount.earn(points)
    }

    @Transactional
    fun usePoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        if (!pointAccount.canDeduct(points)) {
            throw InsufficientAmountException(pointAccount.totalPoints())
        }

        val newTransaction = factory.createUseTransaction(accountId = pointAccount.accountId, amount = points)
        transactionRepository.save(newTransaction)
        pointAccount.deduct(points)
    }

    @Transactional(readOnly = true)
    fun loadHistory(memberId: String): PointHistoryDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        val transactions = transactionRepository.findAll(pointAccount.accountId)

        return PointHistoryDto(transactions.map { TransactionDto(type = it.type, amount = it.points) })
    }

    @Transactional
    fun cancelPoint(memberId: String, points: Long) {
        TODO()
    }

}