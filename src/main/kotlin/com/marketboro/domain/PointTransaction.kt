package com.marketboro.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class PointTransaction(
    @Embedded
    @AttributeOverride(name = "id", column = Column(name = "account_id", length = 40))
    val accountId: AccountId,

    @Column(nullable = false)
    var points: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: TransactionType,

    @Column(nullable = false)
    val transactionDate: LocalDateTime = LocalDateTime.now()
){
    @Id
    @TableGenerator(name = "PointTransactionIdGenerator", table = "sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PointTransactionIdGenerator")
    private var id: Long? = null
}