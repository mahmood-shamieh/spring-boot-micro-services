package com.example.buildingservice;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity(name = "buildings")
@Data
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String address;
    @Column(columnDefinition = "TEXT NULL")
    private String description;
    private double price;
    private double space;
    @Column(columnDefinition = "TEXT NULL")
    private String details;
    private String statue;
    private boolean available;
    private int roomNumber;
    private int floorNumber;
    @Column(columnDefinition = "TEXT NULL")
    private String mediaIds;
    private long userId;
}
