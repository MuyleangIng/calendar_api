package com.example.idata.service;

import com.example.idata.entity.EntityMetadata;
import com.example.idata.entity.AttributeMetadata;
import com.example.idata.repository.EntityMetadataRepository;
import com.example.idata.repository.AttributeMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SchemaService {

    @Autowired
    private EntityMetadataRepository entityMetadataRepository;

    @Autowired
    private AttributeMetadataRepository attributeMetadataRepository;

    public EntityMetadata createEntity(String name, List<Map<String, String>> attributes) {
        EntityMetadata entityMetadata = new EntityMetadata(name);
        entityMetadata = entityMetadataRepository.save(entityMetadata);

        for (Map<String, String> attr : attributes) {
            AttributeMetadata attribute = new AttributeMetadata(
                    entityMetadata.getId(),
                    attr.get("name"),
                    attr.get("type")
            );
            attributeMetadataRepository.save(attribute);
        }

        return entityMetadata;
    }

    public Optional<EntityMetadata> getEntity(Long id) {
        return entityMetadataRepository.findById(id);
    }

    public List<EntityMetadata> getAllEntities() {
        return entityMetadataRepository.findAll();
    }

    public List<AttributeMetadata> getEntityAttributes(Long entityId) {
        return attributeMetadataRepository.findByEntityId(entityId);
    }
}
