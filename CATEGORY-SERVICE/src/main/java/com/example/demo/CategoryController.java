package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    CategoryService service;
    @GetMapping("/{id}")
    public CatEntity findCategory(@PathVariable(name = "id",required = true) int id){
        return service.findEntity(id);
    }
    @GetMapping("/{id}/users/{uid}")
    public Map findCategory(@PathVariable(name = "id",required = true) int id , @PathVariable(name = "uid",required = true) int uid){
        return service.findEntityWithUser(id,uid);
    }
    @DeleteMapping("/{id}")
    public boolean deleteCategory(@PathVariable(name = "id",required = true) int id){
        return service.deleteEntity(id);
    }
    @PostMapping("/")
    public CatEntity addCategory(@RequestBody CatEntity category){
        return service.addCategory(category);
    }
}
