package com.jaehyun.demo.core.repository;

import com.jaehyun.demo.core.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
