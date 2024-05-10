package com.example.idata.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AttributeMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long entityId;  // Foreign key to the entity this attribute belongs to
    private String name;     // Name of the attribute
    private String type;     // Type of the attribute (String, Integer, etc.)

    // Constructors, getters, and setters
    public AttributeMetadata() {}

    public AttributeMetadata(Long entityId, String name, String type) {
        this.entityId = entityId;
        this.name = name;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
