package com.marketboro.controller

import com.marketboro.controller.helper.*
import com.marketboro.controller.helper.TestConst.POINTS_EARNING
import com.marketboro.controller.helper.TestConst.POINTS_USING
import com.marketboro.domain.AccountId
import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccount
import com.marketboro.domain.TransactionType
import com.marketboro.infra.PointAccountJpaRepository
import com.marketboro.infra.PointTransactionJpaRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointHistoryIntegrationTest(
    private val testClient: WebTestClient,
    private val accountRepository: PointAccountJpaRepository,
    private val transactionRepository: PointTransactionJpaRepository
) : FunSpec({
    val idGenerator = TestIdGenerator()
    val existingMemberId = MemberId(idGenerator.generate())
    val existingAccountId = AccountId(idGenerator.generate())
    val notExistingMemberId = MemberId(idGenerator.generate())
    lateinit var pointAccount: PointAccount

    beforeTest {
        transactionRepository.deleteAll()
        accountRepository.deleteAll()

        pointAccount = PointAccount(existingAccountId, existingMemberId)
        accountRepository.save(pointAccount)
    }

    test("등록된 회원의 적립금 사용/적립 내역를 조회할 수 있다") {
        testClient.earnPoint(existingMemberId, points = POINTS_EARNING)
        testClient.usePoint(existingMemberId, points = POINTS_USING)

        val res = testClient.get()
            .uri("/api/members/$existingMemberId/points")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(object : ParameterizedTypeReference<TestPointHistoryDto>() {})
            .returnResult().responseBody!!

        res.transactions shouldContain TestTransactionDto(type = TransactionType.EARN, amount = POINTS_EARNING)
        res.transactions shouldContain TestTransactionDto(type = TransactionType.USE, amount = -POINTS_USING)
    }

    test("등록되지 않은 회원의 적립금 사용/적립 내역를 조회할 수 없다") {
        val res = testClient.get()
            .uri("/api/members/$notExistingMemberId/points")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.MEMBER_NOT_FOUND
    }

})
