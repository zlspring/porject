package com.aohui.btcorg.component

import com.aohui.btcorg.entity.AccountEntity
import com.aohui.btcorg.model.dto.AccountLoginInfoDto
import com.aohui.btcorg.util.UUIDUtil
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoginManager(
        val redissonClient: RedissonClient
) {

    val logger = LoggerFactory.getLogger(LoginManager::class.java)

    //token-obj
    val loginMap = redissonClient.getMap<String, AccountLoginInfoDto>("btcOrgLoginMap")
    //uid-token
    val loginUidMap = redissonClient.getMap<String, String>("btcOrgLoginUidMap")

    /**
     * 存入用户登录信息
     */
    @Synchronized
    fun login(user: AccountEntity, ip: String): String {
        loginOut(user.uid.toString())
        val token = UUIDUtil.genUUID32()
        loginMap.fastPut(token, AccountLoginInfoDto(
                uid = user.uid.toString(),
                userName = user.username,
                email = user.email,
                ip = ip
        ))
        logger.debug(loginMap[token].toString())
        loginUidMap.fastPut(user.uid.toString(), token)
        return token
    }

    /**
     * 登出用户
     */
    @Synchronized
    fun loginOut(uid: String) {
        if (loginUidMap.containsKey(uid)) {
            val token = loginUidMap[uid]
            loginMap.fastRemove(token)
            loginUidMap.fastRemove(token)
        }
    }

    fun findAccount(token: String): AccountLoginInfoDto? {
        return loginMap[token]
    }

    fun userName(token: String): AccountLoginInfoDto? {
        return loginMap[token]
    }
}