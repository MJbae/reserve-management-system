package com.mj.domain

import jakarta.persistence.Embeddable

@Embeddable
data class MemberId(
    private val id: String
) {
    override fun toString(): String {
        return this.id
    }
}