package com.example.demo;

import org.eclipse.jgit.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    @Autowired
    UserRepo repo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FormatFactory factory;

    public ResponseEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        try {
            userEntity = repo.save(userEntity);
            return ResponseEntity.created(URI.create("http://USER-SERIVE/users/"+userEntity.getId())).body(userEntity);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            return ResponseEntity.badRequest().body("There is an error happened");
        }

    }

    public boolean deleteUser(int id) {
        Optional<UserEntity> deletedEntity = repo.findById(Long.valueOf(id));
        if (deletedEntity.isEmpty()) {
            return false;
        } else {
            repo.delete(deletedEntity.get());
            return true;
        }
    }

    public UserDto findUser(int id) {
        Optional<UserEntity> foundedEntity = repo.findById(Long.valueOf(id));
        UserDto userData;
        if(foundedEntity.isEmpty())
            return null;
        else
        {
            String url = "http://FILE-SERVICE/files/"+foundedEntity.get().getProfileMediaId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            try {
                ResponseEntity<UploadFileEntityDto> response =restTemplate.getForEntity(url,UploadFileEntityDto.class);
                return factory.convertUserEntityToDto(foundedEntity.get(),response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public UserDto findUserByUserName(String username) {
        Optional<UserEntity> foundedEntity = repo.findByUsername(username);
        if(foundedEntity.isEmpty())
            return null;
        else {
            String url = "http://FILE-SERVICE/files/"+foundedEntity.get().getProfileMediaId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            try {
                ResponseEntity<UploadFileEntityDto> response =restTemplate.getForEntity(url,UploadFileEntityDto.class);
                return factory.convertUserEntityToDto(foundedEntity.get(),response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public List<UserDto> getAll() {
        return repo.findAll().stream().map(userEntity -> {
            String url = "http://FILE-SERVICE/files/"+userEntity.getProfileMediaId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            try {
                ResponseEntity<UploadFileEntityDto> response =restTemplate.getForEntity(url,UploadFileEntityDto.class);
                return factory.convertUserEntityToDto(userEntity,response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    public Page getPaginationUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<UserEntity> usersPage = repo.findAll(pageable);
        Page<UserDto> usersPage = new PageImpl<>(repo.findAll(pageable).map(userEntity -> {
            String url = "http://FILE-SERVICE/files/"+userEntity.getProfileMediaId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            try {
                ResponseEntity<UploadFileEntityDto> response =restTemplate.getForEntity(url,UploadFileEntityDto.class);
                return factory.convertUserEntityToDto(userEntity,response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).stream().collect(Collectors.toList()));

        return usersPage;
    }
}
