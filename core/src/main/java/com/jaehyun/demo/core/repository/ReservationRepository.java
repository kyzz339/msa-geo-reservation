package com.jaehyun.demo.core.repository;

import com.jaehyun.demo.core.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
