package com.example.buildingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/buildings")
public class BuildingController {
    @Autowired
    BuildingService buildingService;
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/")
    public ResponseEntity addBuilding(@RequestBody BuildingEntity buildingEntity) {
        BuildingEntity addedEntity = this.buildingService.addBuilding(buildingEntity);
        if (addedEntity == null)
            return ResponseEntity.internalServerError().body(null);
        else {
            return ResponseEntity.created(URI.create("http://BUILDING-SERVICE/buildings/" + addedEntity.getId())).body(addedEntity);
        }

    }

    @PostMapping("/random")
    public ResponseEntity addRandomBuilding() throws JsonProcessingException {
        BuildingEntity addedEntity = this.buildingService.addRandomBuilding();
        if (addedEntity == null)
            return ResponseEntity.internalServerError().body("");
        else return ResponseEntity.ok(addedEntity);
    }

    @PostMapping("/uploadMedia")
    public ResponseEntity uploadMedia(@RequestParam(name = "files") MultipartFile[] files, @RequestParam(name = "id") int id) {
        System.out.println(files
                .length);
        return ResponseEntity.ok(this.buildingService.uploadMedia(Arrays.stream(files).toList(), id));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(name = "id") int id) {
        try {
            BuildingDto buildingDto = this.buildingService.findBuilding(id);
            return ResponseEntity.ok().body(buildingDto);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("/getDtoStyle")
    public ResponseEntity getDtoStyle() throws JsonProcessingException {
        List<UploadFileEntityDto> files = new LinkedList<>();
        files.add(new UploadFileEntityDto());
        files.add(new UploadFileEntityDto());
        files.add(new UploadFileEntityDto());
        List<Integer> mediaIds = new LinkedList<>();
        mediaIds.add(1);
        mediaIds.add(2);
        mediaIds.add(3);
        mediaIds.add(4);
        Map details = new LinkedHashMap();
        details.put("hello", "hellov");
        details.put("add", "addv");
        System.out.println(mapper.writeValueAsString(files));
        System.out.println(mapper.writeValueAsString(mediaIds));
        System.out.println(mapper.writeValueAsString(details));
        return ResponseEntity.ok(BuildingDto.builder()
                .media(files)
                .mediaIds(mediaIds)
                .details(details)
                .build());
    }

    //
    @GetMapping("/getEntityStyle")
    public ResponseEntity getEntityStyle() throws JsonProcessingException {
        BuildingEntity entity = new BuildingEntity();
        Map details = new LinkedHashMap();
        details.put("hello", "hellov");
        details.put("add", "addv");
        List<Long> mediaIds = new LinkedList<>();
        mediaIds.add(Long.valueOf(1));
        mediaIds.add(Long.valueOf(2));
        mediaIds.add(Long.valueOf(3));
        System.out.println(mapper.writeValueAsString(mediaIds));
        System.out.println(mapper.writeValueAsString(details));
        entity.setMediaIds(mapper.writeValueAsString(mediaIds));
        entity.setDetails(mapper.writeValueAsString(details));
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/all")
    public ResponseEntity getAllBuildings() {
        try {
            return ResponseEntity.ok(this.buildingService.getAllBuildings());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/page/pageNumber={pageNumber}&pageSize={PageSize}")
    public Page getPaginationBuilding(@PathVariable(name = "pageNumber") int pageNumber, @PathVariable(name = "PageSize") int pageSize) {
        return this.buildingService.getPaginationBuildings(pageSize, pageNumber);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBuilding(@PathVariable(name = "id") int id) throws JsonProcessingException {
        return this.buildingService.deleteBuilding(id) ? ResponseEntity.ok().build() :
                ResponseEntity.noContent().build();
    }
    @DeleteMapping("/media")
    public ResponseEntity deleteMediaFromBuilding(@RequestParam(name = "mediaId") int mediaId,@RequestParam(name = "buildingId") int buildingId) throws JsonProcessingException {
        return this.buildingService.deleteMediaFromBuilding(mediaId,buildingId) ? ResponseEntity.ok().build() :
                ResponseEntity.noContent().build();
    }

}
