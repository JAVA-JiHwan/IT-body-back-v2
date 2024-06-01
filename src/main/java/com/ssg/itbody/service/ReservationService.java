package com.ssg.itbody.service;

import com.ssg.itbody.dto.ReservationRequestDTO;
import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.ReservationRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    public Reservation saveReservation(ReservationRequestDTO reservationRequestDTO) {
        UserEntity trainer = userService.findById(reservationRequestDTO.getTrainerId());

        // 회원 정보 가져옴
        UserEntity member = userService.findById(reservationRequestDTO.getMemberId());

        Reservation reservation = Reservation.builder()
                .trainer(trainer)
                .member(member)
                .title(reservationRequestDTO.getTitle())
                .startTime(reservationRequestDTO.getStartTime())
                .endTime(reservationRequestDTO.getEndTime())
                .description(reservationRequestDTO.getDescription())
                .color(reservationRequestDTO.getColor())
                .build();

        return reservationRepository.save(reservation);
    }


    public List<ReservationRequestDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationRequestDTO(
                        reservation.getStartTime(),
                        reservation.getEndTime(),
                        reservation.getColor(),
                        reservation.getMember().getUserId())) // Assuming UserEntity has getUserId() method
                .collect(Collectors.toList());
    }
}