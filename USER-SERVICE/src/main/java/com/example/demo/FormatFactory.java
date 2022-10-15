package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class FormatFactory {
    public UserDto convertUserEntityToDto(UserEntity userEntity){
        return UserDto.builder()
                .profileMediaId(userEntity.getProfileMediaId())
                .active(userEntity.isActive())
                .address(userEntity.getAddress())
                .age(userEntity.getAge())
                .balance(userEntity.getBalance())
                .createdAt(userEntity.getCreatedAt())
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .FCM_Token(userEntity.getFCM_Token())
                .lastName(userEntity.getLastName())
                .levelId(userEntity.getLevelId())
                .firstName(userEntity.getFirstName())
                .gender(userEntity.getGender())
                .mobile(userEntity.getMobile())
                .middleName(userEntity.getMiddleName())
                .username(userEntity.getUsername())
                .phone(userEntity.getPhone())
                .verified(userEntity.isVerified())
                .password(userEntity.getPassword())
                .build();
        
    }
    public UserDto convertUserEntityToDto(UserEntity userEntity , UploadFileEntityDto uploadFileEntityDto ){
        return UserDto.builder()
                .profileMedia(uploadFileEntityDto)
                .profileMediaId(userEntity.getProfileMediaId())
                .active(userEntity.isActive())
                .address(userEntity.getAddress())
                .age(userEntity.getAge())
                .balance(userEntity.getBalance())
                .createdAt(userEntity.getCreatedAt())
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .FCM_Token(userEntity.getFCM_Token())
                .lastName(userEntity.getLastName())
                .levelId(userEntity.getLevelId())
                .firstName(userEntity.getFirstName())
                .gender(userEntity.getGender())
                .mobile(userEntity.getMobile())
                .middleName(userEntity.getMiddleName())
                .username(userEntity.getUsername())
                .phone(userEntity.getPhone())
                .verified(userEntity.isVerified())
                .password(userEntity.getPassword())
                .build();

    }
}
