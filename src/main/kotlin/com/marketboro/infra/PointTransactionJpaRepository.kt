package com.marketboro.infra

import com.marketboro.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointTransactionJpaRepository : PointTransactionRepository, JpaRepository<PointTransaction, Long?> {
    @Query("SELECT pt FROM PointTransaction pt WHERE pt.accountId = :accountId")
    override fun findAll(@Param("accountId") accountId: AccountId): List<PointTransaction>

    @Query("SELECT t FROM PointTransaction t WHERE t.accountId = :accountId AND t.type = 'USE' ORDER BY t.createdAt DESC")
    override fun findLatestUseTrans(@Param("accountId") accountId: AccountId): PointTransaction?

}