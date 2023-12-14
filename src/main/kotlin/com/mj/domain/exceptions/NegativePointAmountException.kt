package com.mj.domain.exceptions

class NegativePointAmountException(currentAmount: Int) :
    IllegalArgumentException("음수의 적립금은 사용될 수 없습니다. 적립금 값: $currentAmount")
