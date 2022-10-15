package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo repo;
    @Autowired
    RestTemplate template;

    public CatEntity addCategory(CatEntity entity){
        return repo.save(entity);
    }
    public boolean deleteEntity(int id){
        Optional<CatEntity> deletedEntity = repo.findById(Long.valueOf(id));
        if(deletedEntity.isEmpty())
            return false;
        else{
            repo.delete(deletedEntity.get());
            return true;
        }
    }
    public CatEntity findEntity(int id){
        Optional<CatEntity> foundedEntity = repo.findById(Long.valueOf(id));
        return foundedEntity.isEmpty() ? null : foundedEntity.get();
    }
    public Map findEntityWithUser(int id, int uid){
        Optional<CatEntity> foundedEntity = repo.findById(Long.valueOf(id));
        UserEntity userEntity =template.getForObject("http://USER-SERVICE/users/"+uid ,UserEntity.class );
        HashMap map = new HashMap();
        map.put("user",userEntity);
        map.put("category",foundedEntity.isEmpty() ? null : foundedEntity.get());
        return map;
    }

}
