package com.mj.infra

import com.mj.domain.*
import com.mj.domain.AccountId
import com.mj.domain.PointTransaction
import com.mj.domain.PointTransactionRepository
import com.mj.domain.TransactionType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointTransactionJpaRepository : PointTransactionRepository, JpaRepository<PointTransaction, Long?> {
    @Query(
        """
    SELECT t 
    FROM PointTransaction t 
    WHERE t.accountId = :accountId 
    AND t.type IN :types
    """
    )
    override fun findAll(
        @Param("accountId") accountId: AccountId,
        @Param("types") types: Set<TransactionType>,
        pageReq: PageRequest
    ): Slice<PointTransaction>
}