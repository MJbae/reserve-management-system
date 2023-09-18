package com.marketboro.infra

import com.marketboro.domain.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.awt.print.Pageable

@Repository
interface PointTransactionJpaRepository : PointTransactionRepository, JpaRepository<PointTransaction, Long?> {
    @Query("""
    SELECT t 
    FROM PointTransaction t 
    WHERE t.accountId = :accountId 
    AND t.type IN :types
    ORDER BY t.createdAt DESC
    """)
    override fun findByLatest(
        @Param("accountId") accountId: AccountId,
        @Param("types") types: Set<TransactionType>,
        pageReq: PageRequest
    ): Page<PointTransaction>
}