package com.example.demo;

import org.apache.coyote.Response;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getDTO")
    public UserEntity getUserDTO() {
        return new UserEntity();
    }

    @PostMapping("/random")
    public ResponseEntity addRandomUserData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setUsername(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setActive(false);
        userEntity.setAddress(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setAge(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setBalance(10);
        userEntity.setVerified(true);
        userEntity.setMobile(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setPhone(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setCreatedAt(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setEmail(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setFCM_Token(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setFirstName(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setLastName(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setGender(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setMiddleName(USERSERVICEMAINCLASS.getRandomString(20));
        userEntity.setLevelId(1);
        return userService.addUser(userEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity findUser(@PathVariable(name = "id", required = true) int id) {
        UserDto foundedUser = userService.findUser(id);
        if (foundedUser == null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(foundedUser);
    }

    @PostMapping("/findUser")
    public ResponseEntity findUser(@RequestParam String username) {
        UserDto foundedUser = userService.findUserByUserName(username);
        if (foundedUser == null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(foundedUser);
    }

    @GetMapping("/all")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/page/pageNumber={pageNumber}&pageSize={pageSize}")
    public Page<UserEntity> getPaginationUsers(@PathVariable(name = "pageNumber", required = true) String pageNumber,
                                               @PathVariable(name = "pageSize", required = true) String pageSize) {
        return userService.getPaginationUsers(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id", required = true) int id) {
        boolean userDeleted = userService.deleteUser(id);
        return userDeleted ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/")
    public ResponseEntity addUser(@RequestBody UserEntity user) {
        return userService.addUser(user);
    }

    @PostMapping(value = "/updateToken")
    public ResponseEntity updateUserToken(@RequestBody Map map) {

        UserEntity foundedUser = this.userRepo.findById((Long) map.get("id")).get();
        if (foundedUser != null) {
            return userService.addUser(foundedUser);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/uploadProfileMedia")
    public ResponseEntity uploadProfileMedia(@RequestParam MultipartFile file,@RequestParam int id) {
        String url = "http://FILE-SERVICE/files/uploadFile";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            MultiValueMap dataAsMap = new LinkedMultiValueMap();
//            dataAsMap.add("file" , file);
            dataAsMap.add("file", new FileSystemResource(convert(file)));
            dataAsMap.add("serviceName" ,"USER-SERVICE");
            System.out.println(dataAsMap.get("file"));
            System.out.println(dataAsMap.get("serviceName"));
            HttpEntity<MultiValueMap> data = new HttpEntity<MultiValueMap>(dataAsMap,httpHeaders);
            ResponseEntity<UploadFileEntityDto> response =restTemplate.postForEntity(url,data,UploadFileEntityDto.class);
            System.out.println(response.getBody());
//            UserEntity userEntity = userService.findUser(id);
//            userEntity.setProfileMediaId(response.getBody().getId());

//            return ResponseEntity.ok().body(addUser(userEntity));
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
    public static File convert(MultipartFile file)
    {
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
}

