package com.mj.domain

import jakarta.persistence.Embeddable

@Embeddable
data class AccountId(
    private val id: String
) {
    override fun toString(): String {
        return this.id
    }
}