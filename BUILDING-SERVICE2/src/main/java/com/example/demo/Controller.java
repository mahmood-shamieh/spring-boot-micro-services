package com.example.demo;


import com.example.demo.commands.AddCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/buildings")
public class Controller {
    private CommandGateway gateway ;
    public Controller(CommandGateway gateway){
        this.gateway = gateway;
    }
    @Autowired
    BuildingRepo repo;
    @PostMapping
    public  String addBuildingEntity(@RequestBody BuildingModel buildingModel){
        System.out.println("add building method called form controller");
        AddCommand command = AddCommand.builder()
                .aggregatorID(UUID.randomUUID().toString())
                .name(buildingModel.getName())
                .roomNumber(buildingModel.getRoomNumber())
                .build();
        String data = this.gateway.sendAndWait(command);
        return data;
    }
}
