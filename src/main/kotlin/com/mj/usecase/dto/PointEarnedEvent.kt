package com.mj.usecase.dto

import com.mj.domain.AccountId

data class PointEarnedEvent(
    val accountId: AccountId,
    val amount: Int
)