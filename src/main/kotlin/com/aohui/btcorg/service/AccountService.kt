package com.aohui.btcorg.service

import com.aohui.btcorg.model.dto.AccountInfoDto
import com.aohui.btcorg.model.dto.SecurityQuestionDto


interface AccountService {

    /**
     * 注册
     *
     * @param accountInfoDto 需要email phone 和password
     */
    fun signUp(accountInfoDto: AccountInfoDto)

    /**
     * 登录
     *
     * @param accountInfoDto 需要email 和password
     * @return 返回token，如果开启二部认证，则需要提供tfa code
     */
    fun signIn(accountInfoDto: AccountInfoDto, ip: String): AccountInfoDto

    /**
     * 设置安全问题
     */
    fun settingSecurityQuestion(dto: SecurityQuestionDto, ip: String)

    /**
     * 验证安全问题
     */
    fun validateSecurityQuestion(dto: SecurityQuestionDto): Boolean

    /**
     * 绑定手机
     */
    fun bindPhone(uid: String, phone: String)

    /**
     * 绑定邮箱
     */
    fun bindEmail(uid: String, email: String)

}