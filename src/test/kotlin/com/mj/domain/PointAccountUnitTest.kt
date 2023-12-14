package com.mj.domain

import com.mj.controller.helper.TestIdGenerator
import com.mj.domain.exceptions.InsufficientAmountException
import com.mj.domain.exceptions.NegativePointAmountException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


class PointAccountUnitTest : FunSpec({
    val idGenerator = TestIdGenerator()
    lateinit var memberId: MemberId
    lateinit var accountId: AccountId
    lateinit var pointAccount: PointAccount

    beforeEach {
        accountId = AccountId(idGenerator.generate())
        memberId = MemberId(idGenerator.generate())
        pointAccount = PointAccount(accountId, memberId)
    }

    test("막 생성된 PointAccount의 적립금은 0이다") {
        pointAccount.totalPoints() shouldBe 0
    }

    test("적립금을 올바르게 추가할 수 있다") {
        pointAccount.addPoints(10)
        pointAccount.addPoints(20)
        pointAccount.addPoints(30)

        pointAccount.totalPoints() shouldBe 60
    }

    test("적립금을 올바르게 차감할 수 있다") {
        pointAccount.addPoints(50)
        pointAccount.deductPoints(20)

        pointAccount.totalPoints() shouldBe 30
    }

    test("음수값의 적립금을 추가할 수 없다") {
        shouldThrow<NegativePointAmountException> {
            pointAccount.addPoints(-10)
        }
    }

    test("사용 가능한 적림급보다 더 많은 액수를 차감할 수 없다") {
        pointAccount.addPoints(10)

        shouldThrow<InsufficientAmountException> {
            pointAccount.deductPoints(20)
        }
    }
})