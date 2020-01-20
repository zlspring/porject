package com.aohui.btcorg.util

import java.util.*

object UUIDUtil {

    fun genUUID32():String{
        return UUID.randomUUID().toString().replace("-", "")
    }
}