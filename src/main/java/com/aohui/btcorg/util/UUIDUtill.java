package com.aohui.btcorg.util;

import java.util.UUID;

public class UUIDUtill {
    public static Integer getUUID(){
        Integer orderId=UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }
}
