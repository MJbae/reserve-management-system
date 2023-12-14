package com.mj.usecase.dto

import com.mj.domain.AccountId

data class PointEvent(
    val accountId: AccountId,
    val amount: Int
)