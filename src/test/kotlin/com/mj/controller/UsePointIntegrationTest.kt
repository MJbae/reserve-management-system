package com.mj.controller

import com.mj.controller.helper.*
import com.mj.controller.helper.TestConst.EXISTING_MEMBER_ID
import com.mj.domain.AccountId
import com.mj.domain.MemberId
import com.mj.domain.PointAccount
import com.mj.infra.PointAccountJpaRepository
import com.mj.infra.PointTransactionJpaRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsePointIntegrationTest(
    private val testClient: WebTestClient,
    private val accountRepository: PointAccountJpaRepository,
    private val transactionRepository: PointTransactionJpaRepository
) : FunSpec({
    val idGenerator = TestIdGenerator()
    val existingMemberId = MemberId(EXISTING_MEMBER_ID)
    lateinit var existingAccountId: AccountId
    lateinit var notExistingMemberId: MemberId
    lateinit var pointAccount: PointAccount
    lateinit var req: TestPointTransactionReq

    beforeTest {
        transactionRepository.deleteAll()
        accountRepository.deleteAll()
        existingAccountId = AccountId(idGenerator.generate())
        notExistingMemberId = MemberId(idGenerator.generate())

        pointAccount = PointAccount(existingAccountId, existingMemberId)
        accountRepository.save(pointAccount)
    }

    test("등록된 회원은 적립금을 사용할 수 있다") {
        val pointsToEarn = 10
        val pointsToUse = 10
        testClient.earnPoint(existingMemberId, points = pointsToEarn)
        req = TestPointTransactionReq(points = pointsToUse)

        testClient.put()
            .uri("/api/members/$existingMemberId/points/use")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(req)
            .exchange()
            .expectStatus().is2xxSuccessful
    }

    test("등록되지 않은 회원은 적립금을 사용할 수 없다") {
        val pointsToUse = 10
        req = TestPointTransactionReq(points = pointsToUse)

        val res = testClient.put()
            .uri("/api/members/$notExistingMemberId/points/use")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(req)
            .exchange()
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.MEMBER_NOT_FOUND
    }

    test("적립된 포인트 보다 많은 금액을 사용할 수 없다") {
        val pointsToEarn = 10
        val pointsToUse = 15
        testClient.earnPoint(existingMemberId, points = pointsToEarn)
        req = TestPointTransactionReq(points = pointsToUse)

        val res = testClient.put()
            .uri("/api/members/$existingMemberId/points/use")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(req)
            .exchange()
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.INSUFFICIENT_POINTS
    }
})
