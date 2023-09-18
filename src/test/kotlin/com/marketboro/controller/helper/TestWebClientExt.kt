package com.marketboro.controller.helper

import com.marketboro.domain.MemberId
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.getTotalPoints(memberId: MemberId): TestTotalPointsDto {
    return this.get()
        .uri("/api/members/$memberId/points/total")
        .exchange()
        .expectStatus().is2xxSuccessful
        .expectBody(object : ParameterizedTypeReference<TestTotalPointsDto>() {})
        .returnResult().responseBody!!
}

fun WebTestClient.earnPoint(memberId: MemberId, points: Long) {
    val req = TestPointTransactionReq(points = points)

    this.post()
        .uri("/api/members/$memberId/points")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(req)
        .exchange()
        .expectStatus().is2xxSuccessful
}

fun WebTestClient.usePoint(memberId: MemberId, points: Long) {
    val req = TestPointTransactionReq(points = points)

    this.put()
        .uri("/api/members/$memberId/points/use")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(req)
        .exchange()
        .expectStatus().is2xxSuccessful
}

fun WebTestClient.cancelPoint(memberId: MemberId) {
    this.put()
        .uri("/api/members/$memberId/points/cancel")
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().is2xxSuccessful
}