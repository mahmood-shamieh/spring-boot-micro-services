package com.example.demo;


import com.example.demo.events.AddEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {
    private BuildingRepo repo;
    public EventHandler(BuildingRepo repo){
        this.repo = repo;
    }
    @org.axonframework.eventhandling.EventHandler
    public void on(AddEvent event){
        System.out.println(" on method called form event handler");
        BuildingEntity entity = new BuildingEntity();
        BeanUtils.copyProperties(event,entity);
        this.repo.save(entity);
    }
}
