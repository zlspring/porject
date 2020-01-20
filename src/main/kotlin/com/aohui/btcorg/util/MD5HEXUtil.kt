package com.aohui.btcorg.util

import org.springframework.util.DigestUtils

object MD5HEXUtil {
    private val slat = "&%5123***&&%%5432$$#@"

    fun encodeMd5(source: String): String = DigestUtils.md5DigestAsHex("$source/${slat}".toByteArray(Charsets.UTF_8))
}