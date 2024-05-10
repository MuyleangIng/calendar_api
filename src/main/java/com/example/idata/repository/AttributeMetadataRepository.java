package com.example.idata.repository;

import com.example.idata.entity.AttributeMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeMetadataRepository extends JpaRepository<AttributeMetadata, Long> {
    List<AttributeMetadata> findByEntityId(Long entityId);
}
