package com.marketboro.controller

import com.marketboro.controller.req.PointTransactionReq
import com.marketboro.usecase.PointAccountService
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.dto.TotalPointsDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/members")
class PointController(
    private val service: PointAccountService
) {
    @GetMapping("/{memberId}/points/total")
    fun getTotalPoints(
        @PathVariable
        memberId: String
    ): TotalPointsDto {
        return service.getTotalPoints(memberId)
    }

    @PostMapping("/{memberId}/points")
    fun earnPoint(
        @PathVariable
        memberId: String,
        @RequestBody
        req: PointTransactionReq
    ) {
        service.earnPoint(memberId, req.points)
    }

    @PutMapping("/{memberId}/points/use")
    fun usePoint(
        @PathVariable
        memberId: String,
        @RequestBody
        req: PointTransactionReq
    ) {
        service.usePoint(memberId, req.points)
    }


    @GetMapping("/{memberId}/points")
    fun loadHistory(
        @PathVariable
        memberId: String,
        @RequestParam(name="pageNum", defaultValue = "0")
        pageNum: Int,
        @RequestParam(name="pageSize", defaultValue = "10")
        pageSize: Int
    ): PointHistoryDto {
        return service.loadHistory(memberId, pageNum, pageSize)
    }

    @PutMapping("/{memberId}/points/cancel")
    fun cancelPoint(
        @PathVariable
        memberId: String
    ) {
        service.cancelPoint(memberId)
    }
}
