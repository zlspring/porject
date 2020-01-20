package com.aohui.btcorg.dao;

import com.aohui.btcorg.entity.MessageEntity;
import com.aohui.btcorg.entity.MessageReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface MessageFDao extends JpaRepository<MessageEntity,Long>
{

//    @Modifying
//    @Query(value = "select message,id from message where Send_time between ?1 and ?2", nativeQuery = true)
//    List<MessageEntity> findBySend_timeBetween(@Param("start")String start ,@Param("end") String end);

    //所有人已发送修改状态
    @Transactional
    @Modifying
    @Query(value = "update message set statue=?1 where message_id=?2 ", nativeQuery = true)
    void updateM(@Param("statues") String statues,@Param("messageID") Integer messageID);

    @Query(value = "select m.message,u.rec_id,u.status from messagetext m ,user_information u where u.message_id = m.id and u.rec_id=?1 and u.status='1'", nativeQuery = true)
    @Modifying
    List<MessageReq>  findUserInfo(@Param("userid") String userid);

    //私人已发送修改状态
    @Transactional
    @Modifying
    @Query(value = "update user_information set status=?1 where message_id=?2 ", nativeQuery = true)
    void updateUI(@Param("statues") String statues,@Param("messageID") Integer messageID);

    //单人登录修改单人群发状态
    @Transactional
    @Modifying
    @Query(value = "update message set statue=?1 where rec_id=?2 ", nativeQuery = true)
    void updateUIStatus(@Param("statues") String statues,@Param("RecID") String RecID);

}
