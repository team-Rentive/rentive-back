package com.rent.rentservice.user.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
public class UserEditor {

    private String name;

    private String nickName;

    private String phoneNumber;

    private String email;

    private String password;

    @Builder
    public UserEditor(String name, String nickName, String phoneNumber, String email, String password) {
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
}
