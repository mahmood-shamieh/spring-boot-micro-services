package com.example.buildingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FormatFactory {
    ObjectMapper mapper = new ObjectMapper();
    public BuildingDto convertFromEntityToDto(BuildingEntity buildingEntity) throws JsonProcessingException {
        return BuildingDto.builder()
                .description(buildingEntity.getDescription())
                .id(buildingEntity.getId())
                .available(buildingEntity.isAvailable())
                .address(buildingEntity.getAddress())
                .details(mapper.readValue(buildingEntity.getDetails(), LinkedHashMap.class))
                .media(mapper.readValue(buildingEntity.getMediaIds(),List.class))
                .floorNumber(buildingEntity.getFloorNumber())
                .name(buildingEntity.getName())
                .price(buildingEntity.getPrice())
                .roomNumber(buildingEntity.getRoomNumber())
                .space(buildingEntity.getSpace())
                .statue(buildingEntity.getStatue())
                .build();
    }
    public BuildingDto convertFromEntityToDto(BuildingEntity buildingEntity, List<UploadFileEntityDto> uploadFilesEntityDto) throws JsonProcessingException {
        return BuildingDto.builder()
                .media(uploadFilesEntityDto)
                .description(buildingEntity.getDescription())
                .id(buildingEntity.getId())
                .available(buildingEntity.isAvailable())
                .address(buildingEntity.getAddress())
                .details(mapper.readValue(buildingEntity.getDetails() == null ? "{}" : buildingEntity.getDetails(), LinkedHashMap.class))
                .media(uploadFilesEntityDto)
                .mediaIds(mapper.readValue(buildingEntity.getMediaIds(),List.class))
                .floorNumber(buildingEntity.getFloorNumber())
                .name(buildingEntity.getName())
                .price(buildingEntity.getPrice())
                .roomNumber(buildingEntity.getRoomNumber())
                .space(buildingEntity.getSpace())
                .statue(buildingEntity.getStatue())
                .build();
    }
    public BuildingDto convertFromEntityToDto(BuildingEntity buildingEntity, List<UploadFileEntityDto> uploadFilesEntityDto,UserDto userDto) throws JsonProcessingException {
        return BuildingDto.builder()
                .media(uploadFilesEntityDto)
                .description(buildingEntity.getDescription())
                .id(buildingEntity.getId())
                .available(buildingEntity.isAvailable())
                .address(buildingEntity.getAddress())
                .details(mapper.readValue(buildingEntity.getDetails() == null ? "{}" : buildingEntity.getDetails(), LinkedHashMap.class))
                .media(uploadFilesEntityDto)
                .mediaIds(mapper.readValue(buildingEntity.getMediaIds(),List.class))
                .floorNumber(buildingEntity.getFloorNumber())
                .name(buildingEntity.getName())
                .price(buildingEntity.getPrice())
                .roomNumber(buildingEntity.getRoomNumber())
                .space(buildingEntity.getSpace())
                .statue(buildingEntity.getStatue())
                .userDto(userDto)
                .build();
    }
}
