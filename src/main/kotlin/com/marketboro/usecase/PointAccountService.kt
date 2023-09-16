package com.marketboro.usecase

import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccountRepository
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import org.springframework.stereotype.Service

@Service
class PointAccountService(
    private val repository: PointAccountRepository
) {
    fun getTotalPoints(memberId: String): TotalPointsDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw IllegalArgumentException("존재하지 않는 회원입니다")
        return TotalPointsDto(totalPoints = pointAccount.totalPoints())
    }

    fun earnPoint(memberId: String, points: Long) {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw IllegalArgumentException("존재하지 않는 회원입니다")
        pointAccount.earn(points)
    }

    fun usePoint(memberId: String, points: Long) {
        TODO()
    }

    fun loadHistory(memberId: String): PointHistoryDto {
        TODO()
    }

    fun cancelPoint(memberId: String, points: Long) {
        TODO()
    }

}