package com.aohui.btcorg.service.impl

import com.aohui.btcorg.component.LoginManager
import com.aohui.btcorg.entity.AccountEntity
import com.aohui.btcorg.model.dto.AccountInfoDto
import com.aohui.btcorg.model.dto.SecurityQuestionDto
import com.aohui.btcorg.model.exception.MyException
import com.aohui.btcorg.repo.AccountRepo
import com.aohui.btcorg.service.AccountService
import com.aohui.btcorg.util.MD5HEXUtil
import com.aohui.btcorg.util.UUIDUtil
import com.aohui.btcorg.util.UUIDUtill
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("AccountService")
class AccountServiceImpl(
        val loginManager: LoginManager,
        val accountRepo: AccountRepo
) : AccountService {

    val pwdExp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()

    val emailExp = "^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\\.[a-z]{2,}\$".toRegex()

    @Transactional
    @Throws(MyException::class)
    override fun signUp(accountInfoDto: AccountInfoDto) {
        if (!pwdExp.matches(accountInfoDto.password!!)) {
            throw MyException("Passwords must be at least 8 characters, at least one letter and one number")
        }

        if (!emailExp.matches(accountInfoDto.email!!)){
            throw MyException("Incorrect mailbox format")
        }

        val newRegister = AccountEntity(
                uid = UUIDUtill.getUUID().toLong(),
                email = accountInfoDto.email!!,
                password = MD5HEXUtil.encodeMd5(
                        accountInfoDto.password ?: "123456"),
                phone = "",
                status = "1",
                username = accountInfoDto.username!!,
                emailnumber = "0"

        )

        val dbRec = accountRepo.findByEmail(newRegister.email)
        if (dbRec != null) {
            throw MyException("duplicate email")
        } else {
            accountRepo.save(newRegister)
        }
    }

    override fun signIn(accountInfoDto: AccountInfoDto, ip: String): AccountInfoDto {
        val user = if (accountInfoDto.email != null) {
            accountRepo.findByEmail(accountInfoDto.email!!)
        } else {
            null
        }

        if (user == null) {
            throw MyException("user not exist")
        } else if (!user.enabled) {
            throw MyException("blocked account")
        } else {
            user.emailVerified = true
            accountRepo.save(user)
            if (!user.emailVerified) {
                throw MyException("Please check your active email")
            }
            accountInfoDto
            val pwdOk: Boolean = user.password == MD5HEXUtil.encodeMd5(accountInfoDto.password!!)
            if (!pwdOk) {
                throw MyException("password error")
            } else {
                //风控放入到登陆管理器里
                val token = loginManager.login(user,
                        ip)
                return AccountInfoDto(
                        email = user.email,
                        phone = user.phone,
                        uid = user.uid,
                        securityQuestionEnabled = false,
                        token = token
                )
            }
        }
    }

    override fun settingSecurityQuestion(dto: SecurityQuestionDto, ip: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateSecurityQuestion(dto: SecurityQuestionDto): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindPhone(uid: String, phone: String) {
        val rec = accountRepo.findByUid(uid)
        if (rec != null) {
            rec.phone = phone
            rec.phoneVerified = true
            accountRepo.save(rec)
        }
    }

    override fun bindEmail(uid: String, email: String) {
        val rec = accountRepo.findByUid(uid)
        if (rec != null) {
            rec.email = email
            rec.emailVerified = true
            accountRepo.save(rec)
        }
    }

}