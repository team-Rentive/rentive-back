package com.rent.rentservice.user.exception;

/**
 * @description 회원가입 이메일 중복 -> Error Throw
 * @author 김승진
 * @since 2023.01.07
 */

public class OverlapUserEmailException extends RuntimeException {
    private static final String MESSAGE = "중복된 이메일이 존재합니다";

    public OverlapUserEmailException() {super(MESSAGE);}

    public OverlapUserEmailException(Throwable cause) {super(MESSAGE, cause);}
}