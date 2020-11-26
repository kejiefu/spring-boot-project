package com.mountain.orm.jpa.dao.repository;


import com.mountain.orm.jpa.dao.entity.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDetailRepository extends JpaSpecificationExecutor<UserDetailEntity>, JpaRepository<UserDetailEntity, Long> {

    UserDetailEntity findByHobby(String hobby);

}