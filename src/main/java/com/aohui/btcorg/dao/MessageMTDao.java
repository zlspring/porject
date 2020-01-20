package com.aohui.btcorg.dao;

import com.aohui.btcorg.entity.MessageTextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface MessageMTDao extends JpaRepository<MessageTextEntity,Long> {

    @Modifying
    @Query(value = "select message,id,pd_ate,rec_id,status,send from `messagetext` where pd_ate between ?1 and ?2 and status ='1'", nativeQuery = true)
    List<MessageTextEntity> findByPd_ate(@Param("start")String start , @Param("end") String end);

    //所有人已发送修改状态
    @Transactional
    @Modifying
    @Query(value = "update messagetext set status=?1 where id=?2 ", nativeQuery = true)
    void updateMT(@Param("statues") String statues,@Param("id") String messageID);

    //私人已发送修改状态
    @Transactional
    @Modifying
    @Query(value = "update messagetext set status=?1 where id=?2 ", nativeQuery = true)
    void updateUT(@Param("statues") String statues,@Param("id") String messageID);

}
