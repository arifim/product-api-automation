package com.ari.mystore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Getter 
@Setter 
public class Product {

    private Integer id;
    private String name;
    private Double price;
}
