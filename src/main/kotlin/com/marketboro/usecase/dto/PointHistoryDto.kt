package com.marketboro.usecase.dto

import com.marketboro.domain.PointTransaction
import com.marketboro.domain.TransactionType
import org.springframework.data.domain.Slice

data class PointHistoryDto(
    val transactions: List<TransactionDto>,
    val currentPage: Int,
    val hasNextPage: Boolean
) {
    constructor(transSlice: Slice<PointTransaction>) : this(
        transactions = transSlice.content.map { TransactionDto(it.type, it.points) },
        currentPage = transSlice.number,
        hasNextPage = transSlice.hasNext(),
    )
}

data class TransactionDto(
    val type: TransactionType,
    val amount: Int,
)
