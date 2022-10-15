package com.example.demo.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddEvent {
    String aggregatorID;
    String name;
    int roomNumber;
}
