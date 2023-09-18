package com.marketboro.domain

import jakarta.persistence.*

@Entity
class PointAccount(
    @Embedded
    @AttributeOverride(name = "id", column = Column(name = "account_id", unique = true, length = 40))
    val accountId: AccountId,

    @Embedded
    @AttributeOverride(name = "id", column = Column(name = "member_id", unique = true, length = 40))
    val memberId: MemberId,
) {
    @Id
    @TableGenerator(name = "PointAccountIdGenerator", table = "sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PointAccountIdGenerator")
    private var id: Long? = null

    private var points: Long = 0

    fun totalPoints(): Long {
        return points
    }

    fun sumPoints(amount: Long) {
        this.points += amount

    }

    fun canDeduct(amount: Long): Boolean {
        return this.points >= amount
    }


}