package com.aohui.btcorg.dao;

import com.aohui.btcorg.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserDao extends JpaRepository<AccountEntity,Long> {

    List<AccountEntity> findAllBy();

    AccountEntity findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update account_entity set status=?1 where email=?2 ", nativeQuery = true)
    int updateStatus(@Param("status") String status, @Param("email") String email);

    AccountEntity findByUid(String email);

    @Transactional
    @Modifying
    @Query(value = "update account_entity set password=?1 where email=?2 ", nativeQuery = true)
    int updatePassword(@Param("password") String password, @Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "update account_entity set emailnumber=?1 where email=?2 ", nativeQuery = true)
    int updateEmailNumber(@Param("number") String number, @Param("email") String email);

}
