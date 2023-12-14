package com.mj.domain

interface PointAccountRepository {
    fun find(memberId: MemberId): PointAccount?
}