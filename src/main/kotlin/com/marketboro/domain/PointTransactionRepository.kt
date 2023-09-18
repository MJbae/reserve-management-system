package com.marketboro.domain

interface PointTransactionRepository {
    fun findAll(accountId: AccountId): List<PointTransaction>
    fun save(transaction: PointTransaction)
    fun findLatestUseTrans(accountId: AccountId): PointTransaction?
}