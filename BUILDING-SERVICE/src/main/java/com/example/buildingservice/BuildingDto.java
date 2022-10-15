package com.example.buildingservice;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BuildingDto {
    private long id;
    private String name;
    private String address;
    private String description;
    private double price;
    private double space;
    private Map details;
    private String statue;
    private boolean available;
    private int roomNumber;
    private int floorNumber;
    private List<Integer> mediaIds;
    private List<UploadFileEntityDto> media;
    private UserDto userDto;
}
