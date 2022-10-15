package com.example.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @RequestMapping(value = "/users",method = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.PATCH,RequestMethod.POST,RequestMethod.PUT})
    public ResponseEntity fallBackUserService() {
        return ResponseEntity.badRequest().body("no response from user service");
    }

    @RequestMapping(value = "/categories",method = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.PATCH,RequestMethod.POST,RequestMethod.PUT})
    public ResponseEntity fallBackCategoryService() {
        return ResponseEntity.badRequest().body("no response from categories service");
    }


    @RequestMapping(value = "/authentication",method = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.PATCH,RequestMethod.POST,RequestMethod.PUT})
    public ResponseEntity fallBackAuthService() {
        return ResponseEntity.badRequest().body("no response from authentication service");
    }
    @RequestMapping(value = "/file",method = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.PATCH,RequestMethod.POST,RequestMethod.PUT})
    public ResponseEntity fallBackFileService() {
        return ResponseEntity.badRequest().body("no response from file service");
    }
}
