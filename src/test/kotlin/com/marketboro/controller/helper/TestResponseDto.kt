package com.marketboro.controller.helper

import com.marketboro.domain.TransactionType


data class TestTotalPointsDto(
    val totalPoints: Int
)

data class TestPointHistoryDto(
    val transactions: List<TestTransactionDto>
)

data class TestTransactionDto(
    val type: TransactionType,
    val amount: Int
)

data class TestErrorRes(
    val code: Int,
    val message: String?
)

object TestErrorCodes {
    const val MEMBER_NOT_FOUND = 400001

    const val INSUFFICIENT_POINTS = 409001
    const val USE_TRANS_NOT_FOUND = 409002
}