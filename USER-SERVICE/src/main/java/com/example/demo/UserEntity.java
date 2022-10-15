package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique=true)
    private String username;
    private String password;
    private String age;
    private String createdAt;
    @Column(unique=true)
    private String phone;
    @Column(unique=true)
    private String mobile;
    @Column(unique=true)
    private String email;
    private boolean active;
    private boolean verified;
    private String gender;
    private String firstName;
    private String lastName;
    private String middleName;
    private String FCM_Token;
    private int balance;
    private String address;
    private int levelId;
    private long profileMediaId;

}
