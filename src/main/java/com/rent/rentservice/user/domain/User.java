package com.rent.rentservice.user.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @description User Entity
 * @author 김승진
 * @since 2023.01.07
 */

@Entity
@Data
@Table(name = "USER")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String nickName;

    private String phoneNumber;

    private String email;

    private String password;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date regDate;

    @Builder
    public User(String name, String nickName, String phoneNumber, String password, String email) {
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
    }

    public void updateUser(UserEditor userEditor) {
        this.name = userEditor.getName();
        this.nickName = userEditor.getNickName();
        this.email = userEditor.getEmail();
        this.phoneNumber = userEditor.getPhoneNumber();
        this.password = userEditor.getPassword();
    }
}
