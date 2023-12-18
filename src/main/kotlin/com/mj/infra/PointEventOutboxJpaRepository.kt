package com.mj.infra

import com.mj.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PointEventOutboxJpaRepository : PointEventOutboxRepository, JpaRepository<PointEvent, Long?> {

    @Modifying
    @Query("DELETE FROM PointEvent e WHERE e.id IN :ids")
    override fun delete(ids: List<Long?>)
}