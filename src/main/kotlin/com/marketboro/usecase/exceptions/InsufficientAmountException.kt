package com.marketboro.usecase.exceptions

class InsufficientAmountException(currentAmount: Long) :
    IllegalStateException("적립금이 부족합니다. 현재 포인트 적립 합계: $currentAmount")