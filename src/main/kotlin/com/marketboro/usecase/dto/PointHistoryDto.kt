package com.marketboro.usecase.dto

import com.marketboro.domain.TransactionType

data class PointHistoryDto(
    val transactions: List<TransactionDto>
)

data class TransactionDto(
    val type: TransactionType,
    val amount: Long
)