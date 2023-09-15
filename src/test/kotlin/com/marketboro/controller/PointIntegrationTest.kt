package com.marketboro.controller

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointIntegrationTest(
    private val testClient: WebTestClient,
) : FunSpec({
    test("유효한 memeberId로 적립금 합계를 조회할 수 있다.") {
        val memberId = 1L

        val res = testClient.get()
            .uri("/api/members/${memberId}/points/total")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(object : ParameterizedTypeReference<TestTotalPointsDto>() {})
            .returnResult().responseBody!!

        res.totalPoints shouldBe 0
    }
})

data class TestTotalPointsDto(
    val totalPoints: Long
)