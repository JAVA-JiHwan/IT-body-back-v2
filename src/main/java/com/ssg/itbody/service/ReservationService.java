package com.ssg.itbody.service;

import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByTrainer(Long trainerId) {
        return reservationRepository.findByTrainer_UserId(trainerId);
    }

    public List<Reservation> getReservationsByMember(Long memberId) {
        return reservationRepository.findByMember_UserId(memberId);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
