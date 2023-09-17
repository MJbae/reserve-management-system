package com.marketboro.usecase

import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccountRepository
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import com.marketboro.usecase.exceptions.InsufficientAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointAccountService(
    private val repository: PointAccountRepository
) {
    @Transactional(readOnly = true)
    fun getTotalPoints(memberId: String): TotalPointsDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        return TotalPointsDto(totalPoints = pointAccount.totalPoints())
    }

    @Transactional
    fun earnPoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        pointAccount.earn(points)
    }

    @Transactional
    fun usePoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        if(!pointAccount.canDeduct(points)){
            throw InsufficientAmountException(pointAccount.totalPoints())
        }

        pointAccount.deduct(points)
    }

    @Transactional(readOnly = true)
    fun loadHistory(memberId: String): PointHistoryDto {
        TODO()
    }

    @Transactional
    fun cancelPoint(memberId: String, points: Long) {
        TODO()
    }

}