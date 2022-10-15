package com.example.demo;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class BuildingEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    String aggregatorID;
    String name;
    int roomNumber;
}
