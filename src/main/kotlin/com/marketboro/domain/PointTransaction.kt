package com.marketboro.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(indexes = [Index(name = "idx_type_created_at", columnList = "type, createdAt")])
class PointTransaction(
    @Embedded
    @AttributeOverride(name = "id", column = Column(name = "account_id", length = 40))
    val accountId: AccountId,

    @Column(nullable = false)
    val points: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: TransactionType,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @TableGenerator(name = "PointTransactionIdGenerator", table = "sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PointTransactionIdGenerator")
    private var id: Long? = null
}
