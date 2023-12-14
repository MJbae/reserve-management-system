package com.mj.domain

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice

interface PointTransactionRepository {
    fun findAll(accountId: AccountId, types: Set<TransactionType>, pageReq: PageRequest): Slice<PointTransaction>
    fun save(transaction: PointTransaction)
}