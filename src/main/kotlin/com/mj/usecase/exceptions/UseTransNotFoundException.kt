package com.mj.usecase.exceptions

import com.mj.domain.AccountId

class UseTransNotFoundException(id: AccountId) : IllegalArgumentException("적립금 사용 내역이 없습니다. accountId: $id")