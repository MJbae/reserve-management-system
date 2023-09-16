package com.marketboro.controller

import com.marketboro.controller.TestConst.EXISTING_MEMBER_ID
import com.marketboro.controller.TestConst.NOT_EXISTING_MEMBER_ID
import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccount
import com.marketboro.infra.PointAccountJpaRepository
import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EarnPointIntegrationTest(
    private val testClient: WebTestClient,
    private val accountRepository: PointAccountJpaRepository
) : FunSpec({
    val existingMemberId = MemberId(EXISTING_MEMBER_ID)
    val notExistingMemberId = MemberId(NOT_EXISTING_MEMBER_ID)
    lateinit var pointAccount: PointAccount

    beforeTest {
        accountRepository.deleteAll()

        pointAccount = PointAccount(existingMemberId)
        accountRepository.save(pointAccount)
    }


    test("등록된 회원은 적립금을 적립할 수 있다.") {
        testClient.post()
            .uri("/api/members/$existingMemberId/points")
            .exchange()
            .expectStatus().is2xxSuccessful
    }

    test("등록되지 않은 회원은 적립금을 적립할 수 없다.") {
        testClient.post()
            .uri("/api/members/$notExistingMemberId/points")
            .exchange()
            .expectStatus().is4xxClientError
    }

})