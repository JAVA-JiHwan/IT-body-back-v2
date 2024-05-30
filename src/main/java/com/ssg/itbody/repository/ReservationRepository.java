package com.ssg.itbody.repository;

import com.ssg.itbody.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTrainer_UserId(Long trainerId);

    List<Reservation> findByMember_UserId(Long memberId);
}
