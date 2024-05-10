package com.example.idata.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EntityMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // The name of the entity

    // Constructors, getters, and setters
    public EntityMetadata() {}

    public EntityMetadata(String name) {
        this.name = name;
    }



    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
