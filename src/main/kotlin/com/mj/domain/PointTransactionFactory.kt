package com.mj.domain


class PointTransactionFactory {
    fun createEarnTrans(accountId: AccountId, amount: Int): PointTransaction {
        return PointTransaction(type = TransactionType.EARN, accountId = accountId, points = amount)
    }

    fun createUseTrans(accountId: AccountId, amount: Int): PointTransaction {
        return PointTransaction(type = TransactionType.USE, accountId = accountId, points = amount)
    }

    fun createCancelTrans(accountId: AccountId, amount: Int): PointTransaction {
        return PointTransaction(type = TransactionType.CANCEL, accountId = accountId, points = amount)
    }
}

