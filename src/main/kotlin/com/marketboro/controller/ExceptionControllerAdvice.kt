package com.marketboro.controller

import com.marketboro.domain.exceptions.InsufficientAmountException
import com.marketboro.domain.exceptions.NegativePointAmountException
import com.marketboro.usecase.exceptions.MemberNotFoundException
import com.marketboro.usecase.exceptions.UseTransNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice(basePackageClasses = [ExceptionControllerAdvice::class])
class ExceptionControllerAdvice {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [UseTransNotFoundException::class])
    fun handleUseTransNotFoundException(e: UseTransNotFoundException): ErrorRes {
        return ErrorRes(ErrorCodes.USE_TRANS_NOT_FOUND, e.message)
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [InsufficientAmountException::class])
    fun handleInsufficientAmountException(e: InsufficientAmountException): ErrorRes {
        return ErrorRes(ErrorCodes.INSUFFICIENT_POINTS, e.message)
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [IllegalStateException::class])
    fun handleIllegalStateException(e: IllegalStateException): ErrorRes {
        return ErrorRes(ErrorCodes.STATE_CONFLICT, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [NegativePointAmountException::class])
    fun handleNegativePointAmountException(e: NegativePointAmountException): ErrorRes {
        return ErrorRes(ErrorCodes.NEGATIVE_POINT_AMOUNT, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [MemberNotFoundException::class])
    fun handleMemberNotFoundException(e: MemberNotFoundException): ErrorRes {
        return ErrorRes(ErrorCodes.MEMBER_NOT_FOUND, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleIllegalArgumentException(e: IllegalArgumentException): ErrorRes {
        return ErrorRes(ErrorCodes.BAD_REQUEST, e.message)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [Exception::class])
    fun handleException(e: Exception): ErrorRes {
        logger.error("server error occurs", e)
        return ErrorRes(ErrorCodes.INTERNAL_SERVER, e.message)
    }
}


data class ErrorRes(val code: Int, val message: String?)

object ErrorCodes {
    const val BAD_REQUEST = 400000
    const val MEMBER_NOT_FOUND = 400001
    const val NEGATIVE_POINT_AMOUNT = 400002

    const val STATE_CONFLICT = 409000
    const val INSUFFICIENT_POINTS = 409001
    const val USE_TRANS_NOT_FOUND = 409002

    const val INTERNAL_SERVER = 500000
}