package com.mj.controller

import com.mj.controller.helper.*
import com.mj.domain.AccountId
import com.mj.domain.MemberId
import com.mj.domain.PointAccount
import com.mj.infra.PointAccountJpaRepository
import com.mj.infra.PointEventOutboxJpaRepository
import com.mj.infra.PointTransactionJpaRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CancelPointIntegrationTest(
    private val testClient: WebTestClient,
    private val accountRepository: PointAccountJpaRepository,
    private val transactionRepository: PointTransactionJpaRepository,
    private val outboxJpaRepository: PointEventOutboxJpaRepository
) : FunSpec({
    val idGenerator = TestIdGenerator()
    val existingMemberId = MemberId(TestConst.EXISTING_MEMBER_ID)
    lateinit var existingAccountId : AccountId
    lateinit var notExistingMemberId : MemberId
    lateinit var pointAccount: PointAccount

    beforeTest {
        transactionRepository.deleteAll()
        accountRepository.deleteAll()
        outboxJpaRepository.deleteAll()
        existingAccountId = AccountId(idGenerator.generate())
        notExistingMemberId = MemberId(idGenerator.generate())

        pointAccount = PointAccount(existingAccountId, existingMemberId)
        accountRepository.save(pointAccount)
    }

    test("등록된 회원은 사용한 적립금을 취소할 수 있다") {
        val pointsToEarn = 10
        val pointsToUse = 5
        testClient.earnPoint(existingMemberId, points = pointsToEarn)
        testClient.usePoint(existingMemberId, points = pointsToUse)

        testClient.put()
            .uri("/api/members/$existingMemberId/points/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
    }

    test("등록되지 않은 회원은 사용한 적립금을 취소할 수 없다") {
        val res = testClient.put()
            .uri("/api/members/$notExistingMemberId/points/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.MEMBER_NOT_FOUND
    }

    test("사용하지 않은 적립금을 취소할 수 없다") {
        val pointsToEarn = 10
        testClient.earnPoint(existingMemberId, points = pointsToEarn)

        val res = testClient.put()
            .uri("/api/members/$existingMemberId/points/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.USE_TRANS_NOT_FOUND
    }
})
