package com.project.mountain.orm.jpa.dao.repository;


import com.project.mountain.orm.jpa.dao.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}