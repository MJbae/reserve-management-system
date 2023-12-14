package com.mj.usecase

import com.mj.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointTransactionService(
    private val transRepository: PointTransactionRepository,
) {
    private val transFactory = PointTransactionFactory()

    @Transactional
    fun earn(accountId: AccountId, amount: Int) {
        val earnTrans = transFactory.createEarnTrans(accountId, amount)
        transRepository.save(earnTrans)
    }

    @Transactional
    fun use(accountId: AccountId, amount: Int) {
        val useTrans = transFactory.createUseTrans(accountId, amount)
        transRepository.save(useTrans)
    }

    @Transactional
    fun cancel(accountId: AccountId, amount: Int) {
        val cancelTrans = transFactory.createCancelTrans(accountId, amount)
        transRepository.save(cancelTrans)
    }
}
