package com.aohui.btcorg.aop

import com.aohui.btcorg.component.LoginManager
import com.aohui.btcorg.model.dto.AccountLoginInfoDto
import com.aohui.btcorg.model.exception.MyException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.cglib.beans.BeanCopier
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Aspect
@Component
class AopLoginService(
        val loginManager: LoginManager
) {

    private val logger = LoggerFactory.getLogger(AopLoginService::class.java)

    val beanCopier = BeanCopier.create(AccountLoginInfoDto::class.java, AccountLoginInfoDto::class.java, false)

    /**
     * 定义一个切面
     */
    @Pointcut("execution(* com.aohui.btcorg.controller..*.*Email(..))")
    fun aspect() {
    }

    /**
     * 需要在controller里提供httpServletRequest和AccountLoginInfoDto，本方法完成token注入
     */
    @Around("aspect()")
    @Throws(Exception::class)
    fun around(joinPoint: ProceedingJoinPoint): Any {
        logger.info("aop around")
        val args = joinPoint.args

        val httpServletRequest = args.firstOrNull { it is HttpServletRequest }

        if(httpServletRequest != null && httpServletRequest is HttpServletRequest){

            val ip = httpServletRequest.remoteAddr
            val token = httpServletRequest.getHeader("token")

            logger.info("IP: $ip")
            if (token.isNullOrEmpty()) {
                //没有找到token
                throw MyException("Authentication header not found")
            } else {
                val dto = loginManager.findAccount(token)
                if (dto == null) {
                    throw MyException("not login")
                } else {
                    //注入用户基础信息
                    var accountLoginInfoDto = args.firstOrNull { it is AccountLoginInfoDto }
                    if (accountLoginInfoDto != null) {
                        beanCopier.copy(dto, accountLoginInfoDto, null)
                    }
                }
            }
        }

        return joinPoint.proceed()
    }
}