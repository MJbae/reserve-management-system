package com.marketboro.usecase.dto

import com.marketboro.domain.PointTransaction
import com.marketboro.domain.TransactionType
import org.springframework.data.domain.Page

data class PointHistoryDto(
    val transactions: List<TransactionDto>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int
) {
    constructor(transactionsPage: Page<PointTransaction>) : this(
        transactions = transactionsPage.content.map { TransactionDto(it.type, it.points) },
        totalElements = transactionsPage.totalElements,
        totalPages = transactionsPage.totalPages,
        currentPage = transactionsPage.number
    )
}

data class TransactionDto(
    val type: TransactionType,
    val amount: Long
)