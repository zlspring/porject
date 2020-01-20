package com.aohui.btcorg.model.http

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseResponse(
        val msg: String? = null
){

}