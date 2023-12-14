package com.mj.usecase

import com.mj.usecase.dto.TotalPointsDto
import com.mj.usecase.exceptions.MemberNotFoundException
import com.mj.domain.MemberId
import com.mj.domain.PointAccountRepository
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
