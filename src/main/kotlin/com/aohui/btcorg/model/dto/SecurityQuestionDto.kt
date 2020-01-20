package com.aohui.btcorg.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SecurityQuestionDto(

        var uid: String? = "",
        var q1: String? = "",
        var a1: String? = "",
        var q2: String? = "",
        var a2: String? = "",
        var q3: String? = "",
        var a3: String? = ""
)