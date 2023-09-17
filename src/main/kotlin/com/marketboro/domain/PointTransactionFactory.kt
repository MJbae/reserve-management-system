package com.marketboro.domain


class PointTransactionFactory {
    fun createEarnTransaction(accountId: AccountId, amount: Long): PointTransaction {
        return PointTransaction(type = TransactionType.EARN, accountId = accountId, points = amount)
    }

    fun createUseTransaction(accountId: AccountId, amount: Long): PointTransaction {
        return PointTransaction(type = TransactionType.USE, accountId = accountId, points = amount)
    }
}

