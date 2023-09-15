package com.marketboro.controller

import com.marketboro.service.PointAccountService
import com.marketboro.service.dto.TotalPointsDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class PointController(
    private val service: PointAccountService
) {
    @GetMapping("/members/{memberId}/points/total")
    fun getTotalPoints(
        @PathVariable memberId: String
    ): TotalPointsDto {
        val total = service.getTotalPoints(memberId)
        return TotalPointsDto(totalPoints = total)
    }
}
