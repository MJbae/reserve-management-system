package com.marketboro.infra

import com.marketboro.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointTransactionJpaRepository : PointTransactionRepository, JpaRepository<PointTransaction, Long?> {
    @Query("SELECT t FROM PointTransaction t WHERE t.accountId = :accountId")
    override fun findAll(@Param("accountId") accountId: AccountId): List<PointTransaction>
}