package com.aohui.btcorg.dao;

import com.aohui.btcorg.entity.UserInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;

@Transactional
public interface MessageUIDao extends JpaRepository<UserInformationEntity,Long> {

    //单人登录修改单人私发状态
    @Transactional
    @Modifying
    @Query(value = "update user_information set status=?1 where rec_id=?2 ", nativeQuery = true)
    void updateMStatus(@Param("statues") String statues, @Param("RecID") String RecID);
}
