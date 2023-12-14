package com.mj.controller.helper

import com.mj.domain.MemberId
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


fun WebTestClient.earnPoint(memberId: MemberId, points: Int) {
    val req = TestPointTransactionReq(points)

    this.post()
        .uri("/api/members/$memberId/points")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(req)
        .exchange()
        .expectStatus().is2xxSuccessful
}

fun WebTestClient.usePoint(memberId: MemberId, points: Int) {
    val req = TestPointTransactionReq(points)

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