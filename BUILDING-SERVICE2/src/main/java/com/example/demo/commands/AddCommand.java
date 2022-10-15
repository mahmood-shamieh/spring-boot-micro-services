package com.example.demo.commands;


import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class AddCommand {
    @TargetAggregateIdentifier
    private String aggregatorID;
    String name;
    int roomNumber;
}
