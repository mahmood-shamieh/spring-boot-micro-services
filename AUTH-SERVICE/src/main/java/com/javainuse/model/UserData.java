package com.javainuse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
public class UserData implements Serializable {
    long id;
    private String username;
    private String password;
    private String age;
}
