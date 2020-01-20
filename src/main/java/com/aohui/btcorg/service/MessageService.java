package com.aohui.btcorg.service;

import com.aohui.btcorg.entity.EmailReq;
import com.aohui.btcorg.entity.MessageReq;
import java.text.ParseException;

public interface MessageService {
    String messageInsert(MessageReq messageReq);
    void messageFind() throws ParseException;
    String userInformation(String uid);
    String accountStatus(String uid);
    String updateStatus(String emailCode);
    String  activate(EmailReq emailReq);
    String queryUserName(String uid);
    String association(String email);
    String forgetThePassword(String password,String code);
    String changePassword(String email);
}
