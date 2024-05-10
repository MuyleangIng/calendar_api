package com.example.idata.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    public static void buildAndDeployService(String entityName) {
        try {
            // Assuming we have a shell script that builds and deploys the service
            Process process = Runtime.getRuntime().exec("sh build_and_deploy.sh");

            // Wait for the build and deployment to complete
            process.waitFor();

            if (process.exitValue() != 0) {
                System.err.println("Error: Build and deployment script failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void generateController(String entityName, List<Map<String, String>> attributes) {
        String entityNameCamel = capitalize(entityName);

        // Prepare setter code for each attribute
        String setters = attributes.stream()
                .map(attr -> String.format(
                        "item.set%s(updatedItem.get%s());\n",
                        capitalize(attr.get("name")),
                        capitalize(attr.get("name"))
                ))
                .reduce("", (a, b) -> a + b);

        // Controller code as a string
        String controllerCode = String.format("""
package com.example.controller;

import com.example.entity.%1$s;
import com.example.repository.%1$sRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/%2$s")
public class %1$sController {

    @Autowired
    private %1$sRepository %2$sRepository;

    @GetMapping
    public List<%1$s> getAll() {
        return %2$sRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<%1$s> getById(@Pathvariable Long id) {
        Optional<%1$s> item = %2$sRepository.findById(id);
        return item.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public %1$s create(@RequestBody %1$s item) {
        return %2$sRepository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<%1$s> update(@Pathvariable Long id, @RequestBody %1$s updatedItem) {
        return %2$sRepository.findById(id).map(item -> {
            %3$s
            return ResponseEntity.ok(%2$sRepository.save(item));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Pathvariable Long id) {
        return %2$sRepository.findById(id).map(item -> {
            %2$sRepository.delete(item);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
""",
                entityNameCamel, entityName.toLowerCase(), setters);

        // Writing code to a new file
        String filePath = String.format("src/main/java/com/example/controller/%sController.java", entityNameCamel);

        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(controllerCode);
        } catch (IOException e) {
            System.out.println("Error writing controller file: " + e.getMessage());
        }
    }

    // Helper method to capitalize strings
    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
