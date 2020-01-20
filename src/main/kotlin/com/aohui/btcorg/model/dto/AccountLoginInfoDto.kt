package com.aohui.btcorg.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountLoginInfoDto(
        var uid: String?,
        var userName: String?,
        var email: String?,
        var ip: String?
){
}



