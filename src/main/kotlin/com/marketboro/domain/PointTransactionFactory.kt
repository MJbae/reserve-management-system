package com.marketboro.domain


class PointTransactionFactory {
    fun createEarnTrans(accountId: AccountId, amount: Int): PointTransaction {
        return PointTransaction(type = TransactionType.EARN, accountId = accountId, points = amount)
    }

    fun createUseTrans(accountId: AccountId, amount: Int): PointTransaction {
        return PointTransaction(type = TransactionType.USE, accountId = accountId, points = amount)
    }

    fun createCancelTrans(useTrans: PointTransaction): PointTransaction {
        return PointTransaction(type = TransactionType.CANCEL, accountId = useTrans.accountId, points = useTrans.points)
    }
}

