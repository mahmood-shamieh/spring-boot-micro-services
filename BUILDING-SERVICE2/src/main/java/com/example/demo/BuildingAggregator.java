package com.example.demo;


import com.example.demo.commands.AddCommand;
import com.example.demo.events.AddEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class BuildingAggregator {
    public BuildingAggregator() {
    }

    @AggregateIdentifier
    String aggregatorID;
    String name;
    int roomNumber;

    @CommandHandler
    public BuildingAggregator(AddCommand command) {
        System.out.println("add building method called form aggregator");
//        System.out.println(command.toString());
        AddEvent event = new AddEvent();
        BeanUtils.copyProperties(command, event);
//        System.out.println(event.toString());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void On(AddEvent event) {
        System.out.println("on building method called form aggregator ");
        this.name = event.getName();
        this.roomNumber = event.getRoomNumber();
        this.aggregatorID = event.getAggregatorID();
//        BeanUtils.copyProperties(event, this);
//        System.out.println(this.toString());

    }
}
