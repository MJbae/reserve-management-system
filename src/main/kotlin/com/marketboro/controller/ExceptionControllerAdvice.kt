package com.marketboro.controller

import com.marketboro.usecase.exceptions.MemberNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice(basePackageClasses = [ExceptionControllerAdvice::class])
class ExceptionControllerAdvice {
    private val logger = LoggerFactory.getLogger(javaClass)

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

    const val INTERNAL_SERVER = 500000
}