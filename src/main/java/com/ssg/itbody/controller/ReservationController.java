package com.ssg.itbody.controller;

import com.ssg.itbody.dto.ReservationRequestDTO;
import com.ssg.itbody.entity.Reservation;
import com.ssg.itbody.repository.ReservationRepository;
import com.ssg.itbody.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final ReservationRepository reservationRepository;


    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        System.out.println("Received reservation request: " + reservationRequestDTO);
        try {
            Reservation savedReservation = reservationService.saveReservation(reservationRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating reservation: " + e.getMessage());
        }
    }

    @GetMapping("/events")
    public ResponseEntity<List<ReservationRequestDTO>> getEvents() {
        List<ReservationRequestDTO> events = reservationService.findAll();
        return ResponseEntity.ok(events);
    }
}


