package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String name;
    double rate;
    long userId;
}
