package com.example.buildingservice;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    long id;
    private String username;
    private String password;
    private String age;
    private String createdAt;
    private String phone;
    private String mobile;
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
    private UploadFileEntityDto profileMedia;
}
