package com.aohui.btcorg.controller

import com.alibaba.fastjson.JSON
import com.aohui.btcorg.component.LoginManager
import com.aohui.btcorg.model.dto.AccountInfoDto
import com.aohui.btcorg.model.dto.AccountLoginInfoDto
import com.aohui.btcorg.model.http.*
import com.aohui.btcorg.service.AccountService
import com.aohui.btcorg.service.MessageService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("account")
class AccountController(
        val loginManager: LoginManager,
        val accountService: AccountService,
        val messageService: MessageService
) {

    /**
     * 注册
     */
    @PostMapping("signUp")
    fun signUp(@RequestBody req: SignUpRequest): SignUpResponse {


        accountService.signUp(AccountInfoDto(
                email = req.username,
                password = req.password,
                username = req.name
        ))
//        messageService.association(req.username);
        return SignUpResponse(msg = "SUCCESS")
    }

    /**
     * 登录
     */
    @PostMapping("signIn")
    fun signIn(request: HttpServletRequest, @RequestBody req: SignInRequest): SignInResponse {
        val ret = accountService.signIn(
                AccountInfoDto(
                        email = req.username,
                        password = req.password
                ),
                request.remoteAddr
        )
//        val email1 = messageService.accountStatus(req.username);
//        if("2".equals(email1))
//        {
//            return SignInResponse(ret.token!!,"")
//        }else{
//            return SignInResponse("",msg = email1)
//        }
        return SignInResponse(ret.token!!,"")
    }

    /**
     * 登出
     */
    @GetMapping("loginOut")
    fun loginOutEmail(
            request: HttpServletRequest,
            accountLoginInfoDto: AccountLoginInfoDto
    ): BaseResponse {
        loginManager.loginOut(accountLoginInfoDto.uid!!)
        return BaseResponse("ok")
    }

    @PostMapping("userName")
    fun userNameEmail(
        request: HttpServletRequest
    ):String
    {
        val header = request.getHeader("token")
        return JSON.toJSONString(loginManager.userName(header))
    }
}