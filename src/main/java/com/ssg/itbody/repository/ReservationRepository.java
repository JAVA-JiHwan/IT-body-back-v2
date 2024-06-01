package com.ssg.itbody.repository;

import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTrainer_UserId(Long trainerId);

    List<Reservation> findByMember_UserId(Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.startTime >= :startDate AND r.endTime <= :endDate AND r.member = :member AND r.color = :color")
    List<Reservation> findReservations(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("member") UserEntity member,
                                       @Param("color") String color);

    List<Reservation> findAllByStartTimeBetweenAndMember(LocalDateTime startTime, LocalDateTime endTime, UserEntity member);
}