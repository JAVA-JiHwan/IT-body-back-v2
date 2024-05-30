package com.ssg.itbody.controller;

import com.ssg.itbody.dto.ReservationRequestDTO;
import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.UserRepository;
import com.ssg.itbody.service.ReservationService;
import com.ssg.itbody.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        // 현재 로그인한 트레이너의 정보를 가져옴
        UserEntity trainer = userService.findById(reservationRequestDTO.getTrainerId());

        // 회원 정보 가져옴
        UserEntity member = userService.findById(reservationRequestDTO.getMemberId());

        // Reservation 객체 생성
        Reservation reservation = Reservation.builder()
                .trainer(trainer) // 트레이너 정보 설정
                .member(member)   // 회원 정보 설정
                .title(reservationRequestDTO.getTitle())
                .startTime(reservationRequestDTO.getStartTime())
                .endTime(reservationRequestDTO.getEndTime())
                .description(reservationRequestDTO.getDescription())
                .color(reservationRequestDTO.getColor())
                .build();

        // Reservation 저장
        Reservation savedReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(savedReservation);
    }


}
