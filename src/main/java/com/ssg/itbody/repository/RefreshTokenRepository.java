//package com.ssg.itbody.repository;
//
//
//
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//
//    Optional<RefreshToken> findByUserId(Long userId);
//    Optional<RefreshToken> findByRefreshToken(String refreshToken);
//
//    //    RefreshToken findByUserId(Long userId); // 유저번호로 토큰정보 얻기
////    @Query("SELECT r FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
////    RefreshToken findByRefreshToken(@Param("refreshToken") String refreshToken);
////    void deleteByUserId(Long userId);
////
////    @Modifying
////    @Transactional
////    @Query("DELETE FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
////    void deleteByRefreshToken(@Param("refreshToken") String refreshToken);
////
////    @Query("SELECT r.userId FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
////    Long findUserIdByRefreshToken(String refreshToken);
//
//}
