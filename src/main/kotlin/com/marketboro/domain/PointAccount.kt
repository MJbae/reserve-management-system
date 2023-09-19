package com.marketboro.domain

import com.marketboro.domain.exceptions.InsufficientAmountException
import com.marketboro.domain.exceptions.NegativePointAmountException
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

    private var points: Int = 0

    fun totalPoints(): Int {
        return points
    }

    fun addPoints(amount: Int) {
        if (amount < 0) throw NegativePointAmountException(amount)

        points += amount
    }

    fun deductPoints(amount: Int) {
        if (amount < 0) throw NegativePointAmountException(amount)
        if (this.points < amount) throw InsufficientAmountException(this.points)

        points -= amount
    }
}