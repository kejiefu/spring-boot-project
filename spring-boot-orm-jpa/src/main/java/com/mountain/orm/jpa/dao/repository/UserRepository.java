package com.mountain.orm.jpa.dao.repository;


import com.mountain.orm.jpa.dao.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);

    UserEntity findByUserNameOrEmail(String username, String email);

    @Transactional(timeout = 10)
    @Modifying
    @Query("update UserEntity set userName = ?1 where id = ?2")
    int modifyById(String userName, Long id);

    /**
     * nativeQuery = true表示使用sql自己的方言查询，想查什么查什么， 按照字段数据类型返回就行了
     * Param对应sql参数
     */
    @Transactional(timeout = 10)
    @Modifying
    @Query(value = "update t_user set userName = :userName where id = :id", nativeQuery = true)
    int modifyUserNameById(@Param("userName") String userName, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("delete from UserEntity where id = ?1")
    void deleteById(Long id);

    @Query("select u from UserEntity u where u.email = ?1")
    UserEntity findByEmail(String email);

    @Query("select u from UserEntity u")
    Page<UserEntity> findALL(Pageable pageable);

    Page<UserEntity> findByNickName(String nickName, Pageable pageable);

    Slice<UserEntity> findByNickNameAndEmail(String nickName, String email, Pageable pageable);


}