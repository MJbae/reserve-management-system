package com.marketboro.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface PointTransactionRepository {
    fun findByLatest(accountId: AccountId, types: Set<TransactionType>, pageReq: PageRequest): Page<PointTransaction>
    fun save(transaction: PointTransaction)
}