package com.marketboro.controller.helper


data class TestTotalPointsDto(
    val totalPoints: Long
)

data class TestErrorRes(
    val code: Int,
    val message: String?
)

object TestErrorCodes {
    const val BAD_REQUEST = 400000
    const val MEMBER_NOT_FOUND = 400001

    const val STATE_CONFLICT = 409000
    const val INSUFFICIENT_POINTS = 409001

    const val INTERNAL_SERVER = 500000
}