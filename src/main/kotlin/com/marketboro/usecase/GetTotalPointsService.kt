package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.dto.TotalPointsDto
import com.marketboro.usecase.exceptions.MemberNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTotalPointsService(
    private val repository: PointAccountRepository
) {

    @Transactional(readOnly = true)
    fun getTotalPoints(memberId: String): TotalPointsDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))
        return TotalPointsDto(totalPoints = pointAccount.totalPoints())
    }
}
