package com.example.idata.controller;


import com.example.idata.entity.AttributeMetadata;
import com.example.idata.service.SchemaService;
import com.example.idata.utils.CodeGenerator;
import com.example.idata.utils.SchemaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @PostMapping("/create")
    public ResponseEntity<Object> createEntity(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        List<Map<String, String>> attributes = (List<Map<String, String>>) request.get("attributes");

//        EntityMetadata entityMetadata = schemaService.createEntity(name, attributes);
        EntityMetadata entityMetadata = (EntityMetadata) schemaService.createEntity(name, attributes);
        return ResponseEntity.ok(entityMetadata);
    }
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadSchema(@RequestParam("file") MultipartFile file) {
        try {
            // Check if the uploaded file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file.");
            }

            // Read the content of the uploaded file
            String content = new String(file.getBytes());

            // Parse the schema content (you need to implement SchemaParser)
            Map<String, Object> schema = SchemaParser.parseSchema(content);

            String name = (String) schema.get("name");
            List<Map<String, String>> attributes = (List<Map<String, String>>) schema.get("attributes");

            // Create entity and attributes using the parsed schema
            EntityMetadata entityMetadata = (EntityMetadata) schemaService.createEntity(name, attributes);

            // Optionally, you can generate the corresponding controller class here

            // Optionally, you can deploy the new service here

            return ResponseEntity.ok(entityMetadata);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the uploaded file.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @PostMapping("/upload")
//    public ResponseEntity<EntityMetadata> uploadSchema(@RequestParam("file") MultipartFile file) {
//        try {
//            // Parse the schema content
//            String content = new String(file.getBytes());
//            Map<String, Object> schema = SchemaParser.parseSchema(content);
//
//            String name = (String) schema.get("name");
//            List<Map<String, String>> attributes = (List<Map<String, String>>) schema.get("attributes");
//
//            // Generate entity metadata and store in the database
//            EntityMetadata entityMetadata = (EntityMetadata) schemaService.createEntity(name, attributes);
//
//            // Generate the corresponding controller class
//            CodeGenerator.generateController(name, attributes);
//
//            // Build and deploy the new service
//            CodeGenerator.buildAndDeployService(name);
//
//            return ResponseEntity.ok(entityMetadata);
//        } catch (Exception e) {
//            System.err.println("Error uploading schema: " + e.getMessage());
//            return ResponseEntity.status(500).body(null);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<com.example.idata.entity.EntityMetadata> getEntity(@PathVariable Long id) {
        Optional<com.example.idata.entity.EntityMetadata> entity = schemaService.getEntity(id);
        return entity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<com.example.idata.entity.EntityMetadata> getAllEntities() {
        return schemaService.getAllEntities();
    }

    @GetMapping("/{id}/attributes")
    public List<AttributeMetadata> getAttributes(@PathVariable Long id) {
        return schemaService.getEntityAttributes(id);
    }
}
