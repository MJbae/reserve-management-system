package com.marketboro.usecase.exceptions

import com.marketboro.domain.AccountId

class UseTransNotFoundException(id: AccountId) : IllegalArgumentException("적립금 사용 내역이 없습니다. accountId: $id")