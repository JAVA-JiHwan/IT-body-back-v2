package com.ssg.itbody.controller;

import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.UserRepository;
import com.ssg.itbody.service.ReservationService;
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


    private final UserRepository userRepository;

    private final ReservationService reservationService;

//    @PostMapping("/create")
//    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation, @RequestParam Long userId) {
//        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));
//
//        if (user.getMembershipGrade().equals("TRAINER")) {
//            Reservation savedReservation = reservationService.saveReservation(reservation);
//            return ResponseEntity.ok(savedReservation);
//        } else {
////            return ResponseEntity.status(HttpStatus.FORBIDDEN);
//        }
//    }


    @GetMapping("/trainer/{trainerId}")
    public List<Reservation> getReservationsByTrainer(@PathVariable Long trainerId) {
        return reservationService.getReservationsByTrainer(trainerId);
    }

    @GetMapping("/member/{memberId}")
    public List<Reservation> getReservationsByMember(@PathVariable Long memberId) {
        return reservationService.getReservationsByMember(memberId);
    }
}
