package com.ssg.itbody.service;


import com.ssg.itbody.dto.AddUserRequestDTO;
import com.ssg.itbody.dto.LoginRequestDTO;
import com.ssg.itbody.dto.LoginResponseDTO;
import com.ssg.itbody.entity.Authority;
import com.ssg.itbody.entity.RefreshToken;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.exception.BusinessException;
import com.ssg.itbody.exception.ExceptionCode;
import com.ssg.itbody.config.jwt.TokenProvider;
import com.ssg.itbody.repository.RefreshTokenRepository;
import com.ssg.itbody.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static javax.swing.text.html.parser.DTDConstants.ID;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }




    @Override
    public void signup(AddUserRequestDTO dto) { // 회원 email, password를 저장하고 password는 암호화

        boolean existEmail = userRepository.existsByEmail(dto.getEmail()); // 존재하는 이메일인지 확인
        UserEntity userNickname = userRepository.findByNickname(dto.getNickname());

        String password = dto.getPassword();
        String passwordCheck = dto.getConfirmPassword();

        dto.setAuthority(Authority.valueOf("USER")); // 회원가입은 기본 USER

        if(!password.equals(passwordCheck)){
            throw new BusinessException(ExceptionCode.PASSWORD_WRONG);
        }

        if(existEmail){
            throw new BusinessException(ExceptionCode.EXIST_EMAIL);
        }

        if(userNickname != null){
            throw new BusinessException(ExceptionCode.EXIST_NICKNAME);
        }


        userRepository.save(UserEntity.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .imageUrl(dto.getImageUrl())
                .authority(dto.getAuthority())
                .build()
        );

    }

    //  persist, merge, remove와 같은 JPA 연산을 트랜잭션이 활성화된 상태가 아닌 곳에서 호출할 때
    //  TransactionRequiredException이 발생합니다. if문 내부에 토큰 삭제구문이 있으므로 transactional 걸어줘야함.
    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 폼에서 입력한 로그인 아이디를 이용해 DB에 저장된 전체 정보 얻어오기
        UserEntity userInfo = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // 유저가 폼에서 날려주는 정보는 id랑 비번인데, 먼저 아이디를 통해 위에서 정보를 얻어오고
        // 비밀번호는 암호화 구문끼리 비교해야 하므로, 이 경우 bCryptEncoder의 .matchs(평문, 암호문) 를 이용하면
        // 같은 암호화 구문끼리 비교하는 효과가 생깁니다.
        // 상단에 bCryptPasswordEncoder 의존성을 생성한 후, if문 내부에서 비교합니다.
                                            // 폼에서 날려준 평문       // 디비에 들어있던 암호문
        if(bCryptPasswordEncoder.matches(loginRequest.getPassword(), userInfo.getPassword())){

            String accessToken = tokenProvider.generateAccessToken(userInfo, ACCESS_TOKEN_DURATION);//2시간동안 유효한 엑세스토큰 발급
            String refreshToken = tokenProvider.generateRefreshToken(userInfo, REFRESH_TOKEN_DURATION);//7일동안 유효한 리프레시토큰 발급


            //새로운 리프레시토큰 refreshToken 엔터티 객체에 담기
            RefreshToken newRefreshToken = new RefreshToken(userInfo.getUserId(), refreshToken);
            // DB에서 userId에 해당하는 refresh토큰 검색
            Optional<RefreshToken> existingToken = Optional.ofNullable(refreshTokenRepository.findByUserId(userInfo.getUserId()));

            // 기존 토근이 있든 없든 갱신하거나 새로 저장합니다.
            refreshTokenRepository.save(existingToken.orElse(newRefreshToken).update(newRefreshToken.getRefreshToken()));
                                        // null이라면 new토큰 저장             // 이미 있다면 업데이트후 저장


            LoginResponseDTO loginResponseDTO = new LoginResponseDTO().builder()
                                                    .accessToken(accessToken)
                                                    .refreshToken(refreshToken)
                                                    .authority(String.valueOf(userInfo.getAuthority()))
                                                    .userId(userInfo.getUserId())
                                                    .email(userInfo.getEmail())
                                                    .nickname(userInfo.getNickname())
                                                    .imageUrl(userInfo.getImageUrl())
                                                    .build();



            // accessToken과 refreshToken 둘다 저장, user정보도 넘김
            return loginResponseDTO;
        } else {
            throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.");
        }
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        refreshToken = refreshToken.replace("\"", ""); // ""로 감싸져서 있어서 토큰 검증이안됨. 영문을모르겠음
        if(!tokenProvider.validToken(accessToken)){
            throw new IllegalArgumentException("유효하지 않은 엑세스토큰입니다.");
        }

        RefreshToken deleteRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);


        if (deleteRefreshToken == null) {
            throw new IllegalArgumentException("유효하지 않은 리프레시토큰입니다.");
        }else {
            refreshTokenRepository.deleteByRefreshToken(deleteRefreshToken.getRefreshToken());
        }


    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }


    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public UserEntity getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()
                -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        return userEntity;
    }

    // 유저 정보 수정
    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        UserEntity userEntity = userRepository.findById(userUpdateDTO.getUserId())

                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        UserEntity updatingUser = UserEntity.builder()
                .userId(userUpdateDTO.getUserId())
                .email(userEntity.getEmail())
                .nickname(userUpdateDTO.getNickname())
                .password(userEntity.getPassword())
                .imageUrl(userUpdateDTO.getImageUrl())
                .authority(userEntity.getAuthority())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(updatingUser);
    }

    @Override
    public LoginResponseDTO socialLogin(LoginResponseDTO loginResponseDTO) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        this.userRepository.delete(userEntity);
    }

    @Override
    public boolean authenticateUser(LoginRequestDTO loginRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            // 인증 성공
            return true;
        }
        // 인증 실패
        return false;
    }

    // 중복 닉네임 검증
    @Override
    public boolean isNicknameAvailable(String nickname) {
        // 닉네임으로 회원 조회
        UserEntity existingUser = userRepository.findByNickname(nickname);
        // 중복되지 않으면 true, 중복되면 false를 반환합니다.
        return existingUser == null;
    }

}