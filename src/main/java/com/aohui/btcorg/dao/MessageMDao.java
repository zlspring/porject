package com.aohui.btcorg.dao;

import com.aohui.btcorg.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageMDao extends JpaRepository<MessageEntity,Long> {}
