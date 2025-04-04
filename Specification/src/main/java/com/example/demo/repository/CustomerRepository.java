package com.example.demo.repository;

import com.example.demo.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>, JpaSpecificationExecutor<CustomerEntity> {

}
