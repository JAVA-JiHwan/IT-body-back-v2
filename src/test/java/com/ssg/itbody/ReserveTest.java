//package com.ssg.itbody;
//
//import com.ssg.itbody.entity.Reservation;
//import com.ssg.itbody.entity.UserEntity;
//import com.ssg.itbody.repository.ReservationRepository;
//import com.ssg.itbody.service.ReservationService;
//import com.ssg.itbody.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest
//@Transactional
//public class ReserveTest {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Test
//    public void reserveTest() {
//        // 예약에 필요한 데이터 생성
//        reservationRepository.findByTrainer_UserId(1L); // 트레이너 ID에 해당하는 유저 정보를 가져옵니다.
//        reservationRepository.findByMember_UserId(2L); // 멤버 ID에 해당하는 유저 정보를 가져옵니다.
//
//        Reservation reservation = Reservation.builder()
//                .trainer(1)
//                .member(1)
//                .title("예약 제목")
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1)) // 예약 종료 시간 설정 예시
//                .description("예약 설명")
//                .color("#ff0000") // 예약 색상 설정 예시
//                .build();
//
//        // 예약 생성
//        Reservation savedReservation = reservationService.saveReservation(reservation);
//    }
//}
