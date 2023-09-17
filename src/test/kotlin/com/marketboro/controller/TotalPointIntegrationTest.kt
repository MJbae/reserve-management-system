package com.marketboro.controller

import com.marketboro.controller.helper.TestConst.EXISTING_MEMBER_ID
import com.marketboro.controller.helper.TestConst.NOT_EXISTING_MEMBER_ID
import com.marketboro.controller.helper.TestConst.POINTS_EARNING
import com.marketboro.controller.helper.TestErrorCodes
import com.marketboro.controller.helper.TestErrorRes
import com.marketboro.controller.helper.TestTotalPointsDto
import com.marketboro.controller.helper.earnPoint
import com.marketboro.domain.MemberId
import com.marketboro.domain.PointAccount
import com.marketboro.infra.PointAccountJpaRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TotalPointIntegrationTest(
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


    test("등록된 회원의 적립금 합계를 조회할 수 있다") {
        testClient.earnPoint(existingMemberId, points = POINTS_EARNING)

        val res = testClient.get()
            .uri("/api/members/$existingMemberId/points/total")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(object : ParameterizedTypeReference<TestTotalPointsDto>() {})
            .returnResult().responseBody!!

        res.totalPoints shouldBe POINTS_EARNING
    }

    test("등록되지 않은 회원의 적립금 합계를 조회할 수 없다") {
        val res = testClient.get()
            .uri("/api/members/$notExistingMemberId/points/total")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(object : ParameterizedTypeReference<TestErrorRes>() {})
            .returnResult().responseBody!!

        res.code shouldBe TestErrorCodes.MEMBER_NOT_FOUND
    }

})
