package com.rent.rentservice.address.userAddress;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//todo @Embeddable @Embedded 학습
@Entity @Data
public class userAddress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAddressID;

}