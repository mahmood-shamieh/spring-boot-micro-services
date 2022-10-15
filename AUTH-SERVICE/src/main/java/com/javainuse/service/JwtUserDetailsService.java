package com.javainuse.service;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> registerUser(UserData userData){
        String url = "http://USER-SERVICE/users/";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<UserData> requestBody = new HttpEntity<>(userData, httpHeaders);
            ResponseEntity<UserData> response =restTemplate.postForEntity(url,requestBody, UserData.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = "http://USER-SERVICE/users/findUser";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        HttpEntity<MultiValueMap<String, Object>> requestBody = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<UserData> response = null;
        try {
            response = restTemplate.postForEntity(url,requestBody,UserData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null && username.equals(response.getBody().getUsername()) ) {

            return new User(response.getBody().getUsername(), response.getBody().getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}