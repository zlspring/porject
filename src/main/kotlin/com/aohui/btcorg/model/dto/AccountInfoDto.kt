package com.aohui.btcorg.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * 用户账户信息，登录登出注册使用
 *
 * @property email
 * @property password
 * @property phone 可选
 * @property inviteCode 邀请码
 * @property tfaType 0 未启用 1 google auth 2 email 3 phone
 * @property tfaCode 二步认证密码
 * @property securityQuestionEnabled 是否已设置安全问题
 * @property enableTFA 已开启二步验证
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountInfoDto(
        var email: String?,
        var password: String? = null,
        var phone: String? = null,
        var inviteCode: String?   = null,
        var uid: Long? = null,
        var token: String? = null,
        var username: String? = null,
        var tfaType:Int? = 0,
        var tfaCode:String? = null,
        var securityQuestionEnabled: Boolean = false,
        var enableTFA: Boolean = false
)