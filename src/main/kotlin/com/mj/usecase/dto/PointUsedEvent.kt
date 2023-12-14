package com.mj.usecase.dto

import com.mj.domain.AccountId

data class PointUsedEvent(
    val accountId: AccountId,
    val amount: Int
)