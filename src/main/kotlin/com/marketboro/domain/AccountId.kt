package com.marketboro.domain

import jakarta.persistence.Embeddable

@Embeddable
data class AccountId(
    val id: String
) {
    override fun toString(): String {
        return this.id
    }
}