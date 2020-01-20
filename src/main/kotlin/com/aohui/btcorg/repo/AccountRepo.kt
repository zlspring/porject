package com.aohui.btcorg.repo

import com.aohui.btcorg.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo : JpaRepository<AccountEntity, Long> {
    fun findByUid(uid: String): AccountEntity?
    fun findByEmail(email:String): AccountEntity?
    fun findByUsername(userName: String):AccountEntity?
}