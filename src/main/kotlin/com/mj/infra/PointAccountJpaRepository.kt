package com.mj.infra

import com.mj.domain.MemberId
import com.mj.domain.PointAccount
import com.mj.domain.PointAccountRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointAccountJpaRepository : PointAccountRepository, JpaRepository<PointAccount, Long?> {
    @Query("SELECT a FROM PointAccount a WHERE a.memberId = :memberId")
    override fun find(@Param("memberId") memberId: MemberId): PointAccount?
}