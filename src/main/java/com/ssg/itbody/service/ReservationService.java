package com.ssg.itbody.service;

import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByTrainer(Long trainerId) {
        return reservationRepository.findByTrainer_UserId(trainerId);
    }

    public List<Reservation> getReservationsByMember(Long memberId) {
        return reservationRepository.findByMember_UserId(memberId);
    }
}
