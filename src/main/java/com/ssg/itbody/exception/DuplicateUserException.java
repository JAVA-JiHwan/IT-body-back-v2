package com.ssg.itbody.exception;


//중복 유저에 대한 예외
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message){
        super(message);
    }
}
