package com.ssg.itbody.service;

import com.ssg.itbody.dto.AddUserRequestDTO;
import com.ssg.itbody.dto.LoginRequestDTO;
import com.ssg.itbody.dto.LoginResponseDTO;
import com.ssg.itbody.dto.UserUpdateDTO;

import com.ssg.itbody.entity.RefreshToken;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.enums.Authority;
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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

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
    public void signup(AddUserRequestDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ExceptionCode.PASSWORD_WRONG);
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(ExceptionCode.EXIST_EMAIL);
        }

        if (userRepository.findByNickname(dto.getNickname()) != null) {
            throw new BusinessException(ExceptionCode.EXIST_NICKNAME);
        }

        UserEntity newUser = UserEntity.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .imageUrl(dto.getImageUrl())
                .authority(Authority.USER)
                .build();

        userRepository.save(newUser);
    }

    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String accessToken = tokenProvider.generateAccessToken(user, ACCESS_TOKEN_DURATION);
            String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);

            RefreshToken newRefreshToken = new RefreshToken(user.getUserId(), refreshToken);
            refreshTokenRepository.save(newRefreshToken.update(refreshToken));

            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .authority(user.getAuthority().name())
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .imageUrl(user.getImageUrl())
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        refreshToken = refreshToken.replace("\"", "");
        if (!tokenProvider.validToken(accessToken)) {
            throw new IllegalArgumentException("Invalid access token.");
        }

        RefreshToken tokenToDelete = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (tokenToDelete == null) {
            throw new IllegalArgumentException("Invalid refresh token.");
        } else {
            refreshTokenRepository.delete(tokenToDelete);
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
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public void updateUser(com.ssg.itbody.service.UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = userRepository.findById(userUpdateDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        userEntity.setNickname(userUpdateDTO.getNickname());
        userEntity.setImageUrl(userUpdateDTO.getImageUrl());
        userEntity.setUpdatedAt(LocalDateTime.now());

        userRepository.save(userEntity);
    }





    @Override
    public LoginResponseDTO socialLogin(LoginResponseDTO loginResponseDTO) {
        // Social login implementation
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        userRepository.delete(userEntity);
    }

    @Override
    public boolean authenticateUser(LoginRequestDTO loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .map(user -> bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .orElse(false);
    }
}
