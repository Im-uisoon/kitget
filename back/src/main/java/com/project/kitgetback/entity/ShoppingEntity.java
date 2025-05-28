package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shopping")
public class ShoppingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "school")
    private String school;

    @Column(name = "class")
    private String className;

    @Column(name = "needs")
    private String needs;

    @Column(name = "price")
    private Integer price;
}
