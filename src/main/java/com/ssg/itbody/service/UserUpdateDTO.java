package com.ssg.itbody.service;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserUpdateDTO {

    private Long userId;
    private String nickname;
    private String imageUrl;
    private byte[] imageData;

}