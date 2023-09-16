package com.marketboro.usecase

import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccountRepository
import org.springframework.stereotype.Service

@Service
class PointAccountService(
    private val repository: PointAccountRepository
) {
    fun getTotalPoints(memberId: String): Long {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw IllegalArgumentException("존재하지 않는 회원입니다")
        return pointAccount.totalPoints()
    }
}