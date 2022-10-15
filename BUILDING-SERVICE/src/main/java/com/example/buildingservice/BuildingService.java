package com.example.buildingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    @Autowired
    BuildingRepo buildingRepo;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FormatFactory factory;

    ObjectMapper mapper = new ObjectMapper();

    public BuildingEntity addBuilding(BuildingEntity buildingEntity) {
        BuildingEntity addEntity = null;
        addEntity = this.buildingRepo.save(buildingEntity);
        return addEntity;
    }

    public BuildingEntity addRandomBuilding() throws JsonProcessingException {
        BuildingEntity addEntity = new BuildingEntity();
        addEntity.setAddress(BuildingServiceApplication.getRandomString(20));
        addEntity.setAvailable(true);
        addEntity.setDescription(BuildingServiceApplication.getRandomString(20));
        Map details = new HashMap();
        details.put("roomNumber", "20");
        details.put("FloorNumber", "12");
        addEntity.setDetails(mapper.writeValueAsString(details));
        addEntity.setFloorNumber(2);
        int[] mediaIds = {1, 2, 3, 4, 5, 6, 7};
        addEntity.setMediaIds(mapper.writeValueAsString(mediaIds));
        addEntity.setName(BuildingServiceApplication.getRandomString(20));
        addEntity.setPrice(2500);
        addEntity.setRoomNumber(25);
        addEntity.setSpace(150.4);
        addEntity.setStatue("available");
        addEntity = this.buildingRepo.save(addEntity);
        return addEntity;
    }

    public BuildingDto uploadMedia(List<MultipartFile> files, int id) {
        BuildingEntity entity = this.buildingRepo.findById(Long.valueOf(id)).get();
        String url = "http://FILE-SERVICE/files/uploadMultipleFiles";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            MultiValueMap data = new LinkedMultiValueMap();
            data.put("files", files.stream().map(file -> new FileSystemResource(convert(file))).collect(Collectors.toList()));
            HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
            ResponseEntity<List> response = restTemplate.postForEntity(url, httpEntity, List.class);
            List<UploadFileEntityDto> finalFiles = mapper.convertValue(response.getBody(), new TypeReference<List<UploadFileEntityDto>>() {
            });
            System.out.println(response.getBody());
            List<Long> mediaIds = new LinkedList<>();
            finalFiles.forEach(o -> {
                mediaIds.add(o.getId());
                System.out.println(o.getClass());
            });
            entity.setMediaIds(mapper.writeValueAsString(mediaIds));
            System.out.println(entity);
            this.buildingRepo.save(entity);
            return factory.convertFromEntityToDto(entity, response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UploadFileEntityDto> getFilesForBuildings(List<Integer> mediaIds) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            List<UploadFileEntityDto> files = new ArrayList<>();
            mediaIds.forEach(integer -> {
                String FileServiceUrl = "http://FILE-SERVICE/files/" + integer;
                ResponseEntity<UploadFileEntityDto> uploadedFileDto = restTemplate.getForEntity(FileServiceUrl, UploadFileEntityDto.class);
                files.add(uploadedFileDto.getBody());
            });
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserDto getUserForBuilding(int userId) {
        try {
            String UserServiceUrl = "http://USER-SERVICE/users/" + userId;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<UserDto> userDtoResponseEntity = restTemplate.getForEntity(UserServiceUrl, UserDto.class);
            return userDtoResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BuildingDto findBuilding(int id) {
        BuildingEntity entity = this.buildingRepo.findById(Long.valueOf(id)).get();
        try {
            return factory.convertFromEntityToDto(entity, this.getFilesForBuildings(mapper.readValue(entity.getMediaIds(), List.class)), this.getUserForBuilding((int) entity.getUserId()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BuildingDto> getAllBuildings() {
        return this.buildingRepo.findAll().stream().map(buildingEntity -> {
            return this.findBuilding((int) buildingEntity.getId());
        }).collect(Collectors.toList());
    }

    public Page getPaginationBuildings(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BuildingDto> page = new PageImpl<>(this.buildingRepo.findAll(pageable)
                .map(buildingEntity -> {
                    try {
                        return this.factory.convertFromEntityToDto(buildingEntity,
                                this.getFilesForBuildings(mapper.readValue(buildingEntity.getMediaIds(), List.class)),
                                this.getUserForBuilding((int) buildingEntity.getUserId()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).stream().collect(Collectors.toList()));
        return page;
    }

    public boolean deleteBuilding(int id) throws JsonProcessingException {
        try {
            BuildingEntity entity = this.buildingRepo.findById(Long.valueOf(id)).get();
            List<Integer> mediaIds = mapper.readValue(entity.getMediaIds(),List.class);
            mediaIds.forEach(integer -> restTemplate.delete("http://FILE-SERVICE/files/"+integer));
            this.buildingRepo.delete(entity);
           return true;
        }catch (Exception e){
           e.printStackTrace();
           return false;
        }
    }

    public static File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convFile;
    }

    public boolean deleteMediaFromBuilding(int mid,int bid) {
        try {
            BuildingEntity entity = this.buildingRepo.findById(Long.valueOf(bid)).get();
            restTemplate.delete("http://FILE-SERVICE/files/"+mid);
            List<Integer> mediaIds = mapper.readValue(entity.getMediaIds(),List.class);
            mediaIds.remove(mediaIds.indexOf(mid));
            entity.setMediaIds(mapper.writeValueAsString(mediaIds));
            this.buildingRepo.save(entity);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
