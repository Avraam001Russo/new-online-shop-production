package com.russozaripov.productservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "price")
    private int price;
    @Column(name = "description")
    private String description;

    public Details(int price, String description) {
        this.price = price;
        this.description = description;
    }
}
