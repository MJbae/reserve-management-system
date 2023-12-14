package com.mj.usecase.dto

import com.mj.domain.AccountId

data class PointCancelledEvent(
    val accountId: AccountId,
    val amount: Int
)