package com.marketboro.domain

interface PointAccountRepository {
    fun find(memberId: MemberId): PointAccount?
}