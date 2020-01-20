package com.aohui.btcorg.controller

import com.aohui.btcorg.model.dto.AccountLoginInfoDto
import com.aohui.btcorg.service.impl.MessageServiceImpl
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("account")
class MessageController(
        val messageService : MessageServiceImpl
) {

    /**
     * 查看登录用户站内信息
     */
    @GetMapping("messagefind")
    fun messagefind(
            request: HttpServletRequest,
            accountLoginInfoDto: AccountLoginInfoDto
    ): String {
        return messageService.userInformation(accountLoginInfoDto.uid!!)
    }
}