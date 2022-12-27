package com.example.webFluxmongoDb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;

    public Item(String name, String description, double price){
        this.name=name;
        this.description=description;
        this.price=price;
    }
}
