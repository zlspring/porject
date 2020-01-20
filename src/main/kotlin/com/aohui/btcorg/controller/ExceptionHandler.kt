package com.aohui.btcorg.controller

import com.aohui.btcorg.model.exception.MyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MyException::class)
    fun exceptionHandler(e: MyException): Map<String,String?> {
        return mapOf(
                "status" to "error",
                "msg" to e.localizedMessage
        )
    }
}