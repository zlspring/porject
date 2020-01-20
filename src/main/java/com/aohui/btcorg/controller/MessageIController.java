package com.aohui.btcorg.controller;

import com.alibaba.fastjson.JSON;
import com.aohui.btcorg.entity.EmailReq;
import com.aohui.btcorg.entity.MessageReq;
import com.aohui.btcorg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Message")
public class MessageIController {

    @Autowired
    private MessageService messageService;

    /**
     * 添加站内信
     * @param messageReq
     * @return
     */
    @RequestMapping("/messageInsertEmail")
    @ResponseBody
    public String messageInsert(@RequestBody MessageReq messageReq)
    {
        return JSON.toJSONString(messageService.messageInsert(messageReq));
    }

    /**
     * 激活接口
     * @param code
     * @return
     */
    @RequestMapping("/updateStatusEmail")
    @ResponseBody
    public String updateStatusEmail(@Param("code") String code)
    {
        return messageService.updateStatus(code);
    }

    /**
     * 自己给自己发送
     * @return
     */
    @RequestMapping("/resendEmail")
    @ResponseBody
    public String resendEmail(@RequestBody EmailReq emailReq)
    {
        return messageService.activate(emailReq);
    }

    /**
     * 重新发送
     * @return
     */
    @RequestMapping("/rendEmail")
    @ResponseBody
    public String rendEmail(@Param("email")String email)
    {
        return messageService.association(email);
    }

    /**
     * 忘记密码发邮件
     * @param email
     * @return
     */
    @RequestMapping("/changePwdEmail")
    @ResponseBody
    public String changePwdEmail(@Param("email")String email)
    {
        return messageService.changePassword(email);
    }

    /**
     * 忘记密码修改
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("/forgetThePasswordEmail")
    @ResponseBody
    public String forgetThePasswordEmail(@Param("password")String password,@Param("code") String code)
    {
        return messageService.forgetThePassword(password,code);
    }
}
